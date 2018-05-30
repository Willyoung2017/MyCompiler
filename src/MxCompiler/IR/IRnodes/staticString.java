package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class staticString extends staticData{
    public String value;
    public static int cnt = 0;
    public staticString(String value, int len){
        super(("str_"+cnt++), len);
        this.value = value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
