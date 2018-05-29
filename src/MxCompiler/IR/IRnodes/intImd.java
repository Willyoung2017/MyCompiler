package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

import java.math.BigInteger;

public class intImd extends intValue{
    private int value;

    public intImd(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
