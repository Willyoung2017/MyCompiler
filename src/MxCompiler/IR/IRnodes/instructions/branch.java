package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.register;

public class branch extends instruction{
    public binaryOp operator;
    public basicBlock jumpto;
    public basicBlock jumpother;

    public branch(){
        operator = null;
        jumpto = null;
        jumpother = null;
    }

    public branch(binaryOp operator, basicBlock jumpto, basicBlock jumpother){
        this.operator = operator;
        this.jumpto = jumpto;
        this.jumpother = jumpother;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() {

    }
}
