package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.basicBlock;

public class branch extends instruction{
    public binaryOp operator;
    public basicBlock jumpto;
    public basicBlock jumpother;

    public branch(){
        operator = null;
        jumpto = null;
        jumpother = null;
    }
}
