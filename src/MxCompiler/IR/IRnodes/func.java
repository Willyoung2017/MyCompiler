package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.typ;
import MxCompiler.IR.IRnodes.instructions.returnInstr;

import java.util.LinkedList;
import java.util.List;

public class func {
    private basicBlock firstBlock;
    private basicBlock lastBlock;
    private typ funcType;
    public List<virturalRegister> parameterList;
    public List<returnInstr> returnInstrList;

    public func(){
        firstBlock = null;
        lastBlock = null;
        funcType = null;
        parameterList = new LinkedList<>();
        returnInstrList = new LinkedList<>();
    }

    public List<basicBlock> getReverseOrder(){
        List<basicBlock> reverseOrder = new LinkedList<>();
        //Todo
        return reverseOrder;
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
