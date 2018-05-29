package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class binaryOpInstr extends instruction{
    public intValue leftOperand;
    public intValue rightOperand;
    public register result;
    public binaryOp operator;

    public binaryOpInstr(){
        leftOperand = null;
        rightOperand = null;
        result = null;
        operator = null;
    }

    public binaryOpInstr(basicBlock itsBlock, binaryOp operator, intValue leftOperand, intValue rightOperand, register result){
        this.itsBlock = itsBlock;
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.result = result;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return result;
    }

    @Override
    public void setDefRegister(register reg) {
        result = reg;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if(leftOperand instanceof register) usedRegister.add((register) leftOperand);
        if(rightOperand instanceof register) usedRegister.add((register) rightOperand);
    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(leftOperand instanceof virturalRegister) leftOperand = allocateMap.get(leftOperand);
        if(rightOperand instanceof virturalRegister) rightOperand = allocateMap.get(rightOperand);
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
