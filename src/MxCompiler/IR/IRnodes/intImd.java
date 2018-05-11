package MxCompiler.IR.IRnodes;

import java.math.BigInteger;

public class intImd extends intValue{
    private BigInteger value;

    public intImd(BigInteger value){
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }
}
