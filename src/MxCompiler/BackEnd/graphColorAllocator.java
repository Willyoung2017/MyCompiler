package MxCompiler.BackEnd;

import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;
import MxCompiler.X86Related.x86RegisterSet;

import java.util.*;

import static MxCompiler.X86Related.x86RegisterSet.r14;
import static MxCompiler.X86Related.x86RegisterSet.r15;
import static MxCompiler.X86Related.x86RegisterSet.rax;

// refer to http://web.cecs.pdx.edu/~mperkows/temp/register-allocation.pdf

public class graphColorAllocator {
    private func curFunction;
    private Map<String, func> funcMap;
    private Stack<graphNode> nodeSet = new Stack<>();
    private Stack<graphNode> smallNode = new Stack<>();
    private Map<virtualRegister, graphNode> vrMap = new HashMap<>();
    private Stack<graphNode> colorOrder = new Stack<>();
    private Set<physicRegister> usedColor = new HashSet<>();
    private Map<virtualRegister, physicRegister> allocateMap = new HashMap<>();
    private int totalColors = 0;
    private Map<graphNode, stackSlot> slotMap = new HashMap<>();
    private List<physicRegister> physicRegisterList;
    private int count = 0;

    public graphColorAllocator(Map<String, func> funcMap, List<physicRegister> physicRegisterList){
        this.funcMap = funcMap;
        this.physicRegisterList = physicRegisterList;
    }

    public void runAllocator(){
        new livenessAnalysis(funcMap).runAnlysis();

        for(func function : funcMap.values()){
               curFunction = function;
               nodeSet.clear();
               smallNode.clear();
               colorOrder.clear();
               vrMap.clear();
               construcGraph();
               colorNode();
               allocateRegister();
               function.vrMap = vrMap;
               vrMap = new HashMap<>();
        }

    }


    private stackSlot getStackSlot(graphNode node, func function){
        stackSlot slot = slotMap.get(node);

        if(slot == null){
            slot = new stackSlot("__slot_"+count++, function);
            function.varItemList.add(slot);
            slotMap.put(node, slot);
        }

        return slot;
    }

    public static class graphNode{
        public Set<graphNode> neighbours = new HashSet<>();
        public Set<graphNode> sameNodes = new HashSet<>();
        public boolean removed = false;
        public int degree = 0;
        public physicRegister preColor = null;
        public register color = null; //stackSlot && phyReg
    }

    private void linkNodes(graphNode node1, graphNode node2){
            node1.neighbours.add(node2);
            node2.neighbours.add(node1);
    }

    private void removeNode(graphNode node){
        if(node.removed) return;
        for(graphNode neighbourNode : node.neighbours){
            if(!neighbourNode.removed) {
                --neighbourNode.degree;
                if(neighbourNode.degree < totalColors && !smallNode.contains(neighbourNode)){
                       smallNode.push(neighbourNode);
                }
            }
        }

        node.removed = true;
        nodeSet.remove(node);
        smallNode.remove(node);
        colorOrder.push(node);
    }

    private graphNode getNode(virtualRegister reg){
        graphNode node = vrMap.get(reg);
        if(node == null){
            node = new graphNode();
            vrMap.put(reg, node);
        }
        return node;
    }

    private void linkVRegs(virtualRegister reg1, virtualRegister reg2){
        graphNode node1 = getNode(reg1);
        graphNode node2 = getNode(reg2);
        linkNodes(node1, node2);
    }

    private void construcGraph(){
        func function = curFunction;
        List<register> paraRegList = function.parameterList;
        for(register reg : paraRegList){
            getNode((virtualRegister) reg);
        }
        for(register reg1 :paraRegList){
            for(register reg2 :paraRegList){
                if(reg1 != reg2){
                    linkVRegs((virtualRegister) reg1, (virtualRegister) reg2);
                }
            }
        }
        for(basicBlock curBlock : function.getReversePreOrder()){
            for(instruction instr = curBlock.getHead(); instr != null; instr = instr.getNext()){
                register definedReg = instr.getDefRegister();
                if(!(definedReg instanceof virtualRegister)) continue;
                graphNode definedNode = getNode((virtualRegister) definedReg);
             /*   if(instr instanceof move){
                    if(((move) instr).srcReg instanceof virtualRegister){
                        graphNode srcNode = getNode((virtualRegister) ((move) instr).srcReg);
                        definedNode.sameNodes.add(srcNode);
                        srcNode.sameNodes.add(definedNode);
                    }
                    for(virtualRegister reg : instr.liveOut){
                        if(reg != ((move) instr).srcReg && reg != definedReg){
                            linkVRegs(reg, (virtualRegister) definedReg);
                        }
                    }
                }
                else {*/
                    for (virtualRegister reg : instr.liveOut) {
                        if (reg != definedReg) {
                            linkVRegs(reg, (virtualRegister) definedReg);
                        }
                    }
            //    }
            }
        }
        vrMap.values().stream().forEach(x->x.degree = x.neighbours.size());
    }
/*
    private void preColor(func function){
        for(basicBlock bb : function.getreversepostOrderVisit()){
            for(instruction instr = bb.getHead(); instr != null; instr = instr.getNext()){
                graphNode node = null;
                if(instr instanceof returnInstr){
                    if(((returnInstr) instr).retReg instanceof virtualRegister) {
                        node = getNode((virtualRegister) ((returnInstr) instr).retReg);
                        node.preColor = rax;
                    }
                }
                else if(instr instanceof ){

                }
            }
        }
    }
*/
    private void colorNode(){
        slotMap.clear();
        nodeSet.addAll(vrMap.values());
        for(graphNode node : nodeSet){
            if(node.degree < totalColors){
                smallNode.add(node);
            }
        }

        while(!nodeSet.isEmpty()){
            while(!smallNode.isEmpty()){
                graphNode curNode = smallNode.peek();
                colorOrder.push(curNode);
                removeNode(curNode);
            }

            //when no nodes < k, choose one to delete
            graphNode leftNode = nodeSet.peek();
            removeNode(leftNode);
        }

        //start coloring
        while(!colorOrder.isEmpty()){
            graphNode tocolorNode =  colorOrder.pop();
            usedColor.clear();
            for(graphNode node : tocolorNode.neighbours){
                if(!node.removed && node.color instanceof physicRegister){
                    usedColor.add((physicRegister) node.color);
                }
            }

            for(graphNode sameNode : tocolorNode.sameNodes){
                register color = sameNode.color;
                if(color instanceof physicRegister && !usedColor.contains(color)){
                    tocolorNode.color = color;
                    break;
                }
            }

            if(tocolorNode.color == null){
                for(physicRegister reg : physicRegisterList){
                    if(!usedColor.contains(reg)){
                        tocolorNode.color = reg;
                        break;
                    }
                }

                //no color, spill in mem
                if(tocolorNode.color == null) {
                    tocolorNode.color = getStackSlot(tocolorNode, curFunction);
                }

            }

            tocolorNode.removed = false;
        }
    }

    private void allocateRegister(){

        for(int i = 0; i < curFunction.parameterList.size(); ++i){
            register para = curFunction.parameterList.get(i);
            register allocated = vrMap.get(para).color;
            curFunction.parameterList.set(i, allocated);
            if(i >= 6) {
                stackSlot slot = new stackSlot(para.getName(), curFunction);
                curFunction.paraSlotList.add(slot);
            }
        }

        for(basicBlock bb : curFunction.getPreOrder()){
            for(instruction instr = bb.getHead(); instr != null; instr = instr.getNext()){
                Collection<register> used = instr.usedRegister;
                if(!used.isEmpty()) {
                    allocateMap.clear();
                    boolean flag = false;
                    for (register usedReg : instr.usedRegister) {
                        if (usedReg instanceof virtualRegister) {
                            physicRegister allocatedReg = null;
                            register color = vrMap.get(usedReg).color;
                            if (color instanceof physicRegister) {
                                allocatedReg = (physicRegister) color;
                            }
                            else if(color instanceof stackSlot){
                                allocatedReg = flag ? x86RegisterSet.rbx :x86RegisterSet.rax;
                                flag = true;
                                instr.linkPrev(new load(bb, color, allocatedReg, 0,8));
                            }

                            allocateMap.put((virtualRegister) usedReg, allocatedReg);

                            //stackSlot slot = getStackSlot((virtualRegister) usedReg, curFunction);
                            //instr.linkPrev(new load(curBlock, slot, allocatedReg, 0, 8));

                        }
                    }
                    instr.resetUsedRegister(allocateMap);
                }

                register defReg = instr.getDefRegister();

                if(defReg instanceof virtualRegister) {
                    physicRegister allocatedReg = null;
                    register color = vrMap.get(defReg).color;

                   if(color instanceof physicRegister){
                        allocatedReg = (physicRegister) color;
                       instr.setDefRegister(allocatedReg);
                    }
                    else if(color instanceof stackSlot){
                        allocatedReg = x86RegisterSet.rbx;
                        instr.linkNext(new store(bb, allocatedReg, color, 0, 8));
                        instr.setDefRegister(allocatedReg);
                        instr = instr.getNext();
                    }
                    //allocateMap.put((virtualRegister) defReg, allocatedReg);

                    //stackSlot slot = getStackSlot((virtualRegister) defReg, function);
                    //slotMap.put((virtualRegister)defReg, slot);
                    //instr.linkNext(new store(curBlock, allocatedReg, slot, 0, 8));
                    //instr.linkNext(new move(curBlock, allocatedReg, slot));
                }
            }
        }
    }

}
