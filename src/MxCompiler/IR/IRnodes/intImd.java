package MxCompiler.IR.IRnodes;

import java.math.BigInteger;

public class intImd extends intValue{
    private int value;

    public intImd(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
