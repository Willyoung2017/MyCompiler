package MxCompiler.IR;

import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRprinter implements IRVisitor{
    private Map<String, func> funcMap;
    private PrintStream out;
    private Set<basicBlock> visitedBlocks = new HashSet<>();
    private Map<String, Integer> counter = new HashMap<>();
    private Map<basicBlock, String> labelMap = new HashMap<>();
    private Map<register, String> registerMap = new HashMap<>();
    private Map<staticData, String> dataMap = new HashMap<>();
    private Map<staticString, String> stringMap = new HashMap<>();

    public IRprinter(Map<String, func> funcMap, PrintStream out){
        this.funcMap = funcMap;
        this.out = out;
    }

    public void runPrinter(){
        funcMap.values().stream().forEach(this::visit);
    }

    @Override
    public void visit(func node) {
        out.printf("func %s ", node.getFuncName());
        for(register paraReg : node.parameterList){
            out.printf("$%s ", getRegName(paraReg));
        }
        out.println("{");
        node.getPreOrder().stream().forEach(this::visit);
        out.println("}");
    }

    private String allocateID(String name, Map<String, Integer> counter){
        Integer cnt = counter.get(name);
        if(cnt == null) cnt = 1;
        else ++cnt;
        counter.put(name, cnt);
        if(cnt == 1){
            return name;
        }
        else{
            return name + "_" +  cnt;
        }
    }

    private String getBlockLabel(basicBlock bb){
        String label = labelMap.get(bb);
        if(label == null){
            label = allocateID(bb.getName(), counter);
            labelMap.put(bb, label);
        }
        return label;
    }

    @Override
    public void visit(basicBlock node) {
        if(node == null || visitedBlocks.contains(node)) return;
        visitedBlocks.add(node);
        out.println("%" + getBlockLabel(node) + ":");
        for(instruction instr = node.getHead(); instr != null; instr = instr.getNext()){
            visit(instr);
        }
        out.println();
    }

    @Override
    public void visit(instruction node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryOpInstr node) {
        out.print("    ");
        String op = null;
        switch (node.operator){
            case BITWISE_INCLUSIVE_OR:
                op = "or";
                break;
            case BITWISE_EXCLUSIVE_OR:
                op = "xor";
                break;
            case BITWISE_AND:
                op = "and";
                break;
            case MOD:
                op = "rem";
                break;
            case MUL:
                op = "mul";
                break;
            case DIV:
                op = "div";
                break;
            case SUB:
                op = "sub";
                break;
            case ADD:
                op = "add";
                break;
            case LEFT_SHIFT:
                op = "shl";
                break;
            case RIGHT_SHIFT:
                op = "ashr";
        }
        visit(node.result);
        out.printf(" = %s ", op);
        visit(node.leftOperand);
        out.print(" ");
        visit(node.rightOperand);
        out.println();
    }

    @Override
    public void visit(branch node) {
        out.printf("    br $cond %%%s %%%s\n\n", getBlockLabel(node.jumpto), getBlockLabel(node.jumpother));
    }

    @Override
    public void visit(cmp node) {
        out.print("    ");
        String op = null;
        switch(node.operator){
            case EQUAL:
                op = "seq";
                break;
            case NOT_EQUAL:
                op = "sne";
                break;
            case LESS:
                op = "slt";
                break;
            case LEQ:
                op = "sle";
                break;
            case GREATER:
                op = "sgt";
                break;
            case GEQ:
                op = "sge";
        }
        //visit(node.result);
        out.printf("$cond");
        out.printf(" = %s ", op);
        visit(node.leftOperand);
        out.print(" ");
        visit(node.rightOperand);
        out.println();
    }

    @Override
    public void visit(funCall node) {
        out.print("    ");
        if(node.returnReg != null){
            visit(node.returnReg);
            out.print(" = ");
        }
        out.printf("call %s ", node.function.getFuncName());
        for(intValue reg : node.parameters){
            visit(reg);
            out.print(" ");
        }
        out.println();
    }

    @Override
    public void visit(heapAllocate node) {
        out.print("    ");
        visit(node.destReg);
        out.print(" = alloc ");
        visit(node.allocSize);
        out.println();
    }

    @Override
    public void visit(jump node) {
        out.printf("    jump %%%s\n\n", getBlockLabel(node.jumpto));
    }

    @Override
    public void visit(load node) {
        out.print("    ");
        visit(node.dest);
        out.printf(" = load %d ", node.accessSize);
        visit(node.addr);
        out.print(" " + node.offset);
        out.println();
}

    @Override
    public void visit(move node) {
        out.print("    ");
        visit(node.destReg);
        out.print(" = move ");
        visit(node.srcReg);
        out.println();
    }

    @Override
    public void visit(returnInstr node) {
        out.print("    ret ");
        visit(node.retReg);
        if(node.retReg == null)
            out.print("-1");
        out.println();
        out.println();
    }

    @Override
    public void visit(store node) {
        out.printf("    store %d ", node.accessSize);
        visit(node.addr);
        out.print(" ");
        visit(node.src);
        out.print(" " + node.offset);
        out.println();
    }

    @Override
    public void visit(unaryOpInstr node) {
        out.print("    ");
        String op = null;
        switch(node.operator){
            case NEG:
                op = "neg";
                break;
            case BITWISE_NOT:
                op = "not";
        }
        visit(node.result);
        out.printf(" = %s ", op);
        visit(node.operand);
        out.println();
    }

    @Override
    public void visit(intValue node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(intImd node) {
        out.print(node.getValue());
    }

    @Override
    public void visit(register node) {
        if(node == null) return;
        node.accept(this);
    }

    private String getRegName(register node){
        String regName = registerMap.get(node);
        if(regName == null){
            if(node.getName() != null){
                regName = allocateID(node.getName(), counter);
                registerMap.put(node, regName);
            }
            else {
                regName = allocateID("tmp", counter);
                registerMap.put(node, regName);
            }
        }
        return regName;
    }

    @Override
    public void visit(virturalRegister node) {
        out.print("$" + getRegName(node));
    }

    @Override
    public void visit(physicRegister node) {

    }

    @Override
    public void visit(stackSlot node) {

    }

    private String dataId(intValue node){
        String name = null;
        if(node instanceof staticString){
            name = stringMap.get(node);
            if(name == null){
                name = ((staticString)node).varName;
                stringMap.put((staticString) node, name);
            }
        }
        else{
            name = dataMap.get(node);
            if(name == null){
                name = ((staticData)node).varName;
                dataMap.put((staticData) node, name);
            }
        }
        return name;
    }

    @Override
    public void visit(staticData node) {
        out.printf("space @%s %d", dataId(node), node.len);
    }

    @Override
    public void visit(staticString node) {
        out.printf("asciiz @%s %s", dataId(node), node.value);
    }
}
