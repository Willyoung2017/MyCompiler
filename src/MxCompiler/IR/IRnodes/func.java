package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.typ;
import MxCompiler.IR.IRnodes.instructions.returnInstr;

import java.util.*;

public class func {
    private basicBlock firstBlock;
    private basicBlock lastBlock;
    private typ funcType;
    public List<virturalRegister> parameterList;
    public List<returnInstr> returnInstrList;
    public List<basicBlock> preOrder = null;
    public List<basicBlock> reversePreOrder = null;
    public func(){
        firstBlock = null;
        lastBlock = null;
        funcType = null;
        parameterList = new LinkedList<>();
        returnInstrList = new LinkedList<>();
    }

    private void preOrderVist(basicBlock curBlock, Set<basicBlock> visited){
        if(visited.contains(curBlock)) return;
        visited.add(curBlock);
        reversePreOrder.add(curBlock);
        for(basicBlock nxtBlock : curBlock.getNext()){
            preOrderVist(nxtBlock, visited);
        }
    }

    private void preOrder_Vist(basicBlock curBlock, Set<basicBlock> visited){
        if(visited.contains(curBlock)) return;
        visited.add(curBlock);
        preOrder.add(curBlock);
        for(basicBlock nxtBlock : curBlock.getNext()){
            preOrderVist(nxtBlock, visited);
        }
    }

    public List<basicBlock> getPreOrder(){
        if(preOrder != null) return preOrder;
        Set<basicBlock> visited = new HashSet<>();
        preOrder_Vist(firstBlock, visited);
        return preOrder;
    }

    public List<basicBlock> getReversePreOrder(){
        if(reversePreOrder != null) return reversePreOrder;
        Set<basicBlock> visited = new HashSet<>();
        preOrderVist(firstBlock, visited);
        Collections.reverse(reversePreOrder);
        return reversePreOrder;
    }

    public void setFuncType(typ funcType){
        this.funcType = funcType;
    }

    public void setFirstBlock(basicBlock firstBlock){
        this.firstBlock = firstBlock;
    }

    public void setLastBlock(basicBlock lastBlock){
        this.lastBlock = lastBlock;
    }

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
