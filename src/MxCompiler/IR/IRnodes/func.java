package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.typ;
import MxCompiler.IR.IRnodes.instructions.returnInstr;

import java.util.*;

public class func {
    private String funcName;
    private basicBlock firstBlock = null;
    private basicBlock lastBlock = null;
    private typ funcType;
    public List<register> parameterList = new LinkedList<>();
    public List<returnInstr> returnInstrList = new LinkedList<>();
    public List<basicBlock> preOrder = null;
    public List<basicBlock> reversePreOrder = null;
    public func(){
        funcName = null;
        funcType = null;
    }

    public func(String funcName, typ funcType){
        this.funcName = funcName;
        this.funcType = funcType;
    }

    private void preOrderVisit(basicBlock curBlock, Set<basicBlock> visited){
        if(visited.contains(curBlock)) return;
        visited.add(curBlock);
        reversePreOrder.add(curBlock);
        for(basicBlock nxtBlock : curBlock.getNext()){
            preOrderVisit(nxtBlock, visited);
        }
    }

    private void preOrder_Visit(basicBlock curBlock, Set<basicBlock> visited){
        if(visited.contains(curBlock)) return;
        visited.add(curBlock);
        preOrder.add(curBlock);
        for(basicBlock nxtBlock : curBlock.getNext()){
            preOrder_Visit(nxtBlock, visited);
        }
    }

    private void bottomup_Visit(basicBlock curBlock, Set<basicBlock> visited){
        if(visited.contains(curBlock)) return;
        visited.add(curBlock);
        for(basicBlock nxtBlock : curBlock.getPred()){
            bottomup_Visit(nxtBlock, visited);
        }
    }
    public void clearUnreachableBlocks(){
        Set<basicBlock> visited = new HashSet<>();
        Set<basicBlock> bothVisited = new HashSet<>();
        if(preOrder == null) {
            preOrder = new LinkedList<>();
            preOrder_Visit(firstBlock, visited);
        }
        visited.clear();
        bottomup_Visit(lastBlock, visited);

        for(basicBlock bb : visited){
            if(preOrder.contains(bb)){
                bothVisited.add(bb);
            }
        }

        for(basicBlock bb : preOrder){
            if(!bothVisited.contains(bb)){
                bb.remove();
            }
        }
        preOrder = null;
    }

    public List<basicBlock> getPreOrder(){
        if(preOrder != null) return preOrder;
        Set<basicBlock> visited = new HashSet<>();
        preOrder = new LinkedList<>();
        preOrder_Visit(firstBlock, visited);
        return preOrder;
    }

    public List<basicBlock> getReversePreOrder(){
        if(reversePreOrder != null) return reversePreOrder;
        Set<basicBlock> visited = new HashSet<>();
        reversePreOrder = new LinkedList<>();
        preOrderVisit(firstBlock, visited);
        Collections.reverse(reversePreOrder);
        return reversePreOrder;
    }

    public void setFirstBlock(basicBlock firstBlock){
        this.firstBlock = firstBlock;
    }

    public void setLastBlock(basicBlock lastBlock){
        this.lastBlock = lastBlock;
    }

    public String getFuncName() { return funcName;}

    public typ getFuncType(){
        return funcType;
    }

    public basicBlock getFirstBlock(){
        return firstBlock;
    }

    public basicBlock getLasstBlock(){
        return lastBlock;
    }
}
