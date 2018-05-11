package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.typ;

import java.util.LinkedList;
import java.util.List;

public class func {
    private basicBlock firstBlock;
    private basicBlock lastBlock;
    private typ funcType;
    public List<virturalRegister> parameterList;

    public func(){
        firstBlock = null;
        lastBlock = null;
        funcType = null;
        parameterList = new LinkedList<>();
    }

    public func(basicBlock firstBlock, basicBlock lastBlock, typ funcType, List<virturalRegister> parameterList){
        this.firstBlock = firstBlock;
        this.lastBlock = lastBlock;
        this.funcType = funcType;
        this.parameterList = parameterList;
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
