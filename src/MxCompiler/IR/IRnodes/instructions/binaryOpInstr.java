package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

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

    public binaryOpInstr(binaryOp operator, intValue leftOperand, intValue rightOperand, virturalRegister result){
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
    public void setUsedRegister() {
        if(leftOperand instanceof register) usedRegister.add((register) leftOperand);
        if(rightOperand instanceof register) usedRegister.add((register) rightOperand);
    }
}
