package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

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
    public cmp(binaryOp operator, intValue leftOperand, intValue rightOperand, register result){
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.result = result;
    }
}
