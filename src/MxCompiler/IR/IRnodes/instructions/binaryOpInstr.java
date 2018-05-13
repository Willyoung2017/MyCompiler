package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.virturalRegister;

public class binaryOpInstr extends instruction{
    public intValue leftOperand;
    public intValue rightOperand;
    public virturalRegister result;
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
    }
}
