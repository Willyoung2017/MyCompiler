package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.virturalRegister;

public class binaryOpInstr extends instruction{
    public intValue leftOperand;
    public intValue rightOperand;
    public virturalRegister result;
    public binaryOp operator;
}
