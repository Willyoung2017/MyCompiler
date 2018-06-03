package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class cmp extends instruction{
    public intValue leftOperand;
    public intValue rightOperand;
    public register result;
    public binaryOp operator;

    public cmp(){
        leftOperand = null;
        rightOperand = null;
        result = null;
        operator = null;
    }
    public cmp(basicBlock itsBlock, binaryOp operator, intValue leftOperand, intValue rightOperand, register result){
        this.itsBlock = itsBlock;
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.result = result;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if(leftOperand instanceof register) usedRegister.add((register) leftOperand);
        if(rightOperand instanceof register) usedRegister.add((register) rightOperand);
    }


    public void setDefRegister(register reg){

    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        if(leftOperand instanceof virtualRegister)  leftOperand = allocateMap.get(leftOperand);
        if(rightOperand instanceof virtualRegister) rightOperand = allocateMap.get(rightOperand);
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
