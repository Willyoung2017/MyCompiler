package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class staticString extends staticData{
    public String value;

    public staticString(String value, int len){
        super("str", len);
        this.value = value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
