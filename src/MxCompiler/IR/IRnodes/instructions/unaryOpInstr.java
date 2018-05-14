package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.UnaryExpression.unaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

public class unaryOpInstr extends instruction{
    public intValue operand;
    public register result;
    public unaryOp operator;

    public unaryOpInstr(){
        operand = null;
        result = null;
        operator = null;
    }

    public unaryOpInstr(unaryOp operator, intValue operand, virturalRegister result){
        this.operand = operand;
        this.operator = operator;
        this.result = result;
    }
}
