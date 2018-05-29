package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class staticData extends register{
    public int len;
    public String varName;

    public staticData(String varName, int len){
        this.varName = varName;
        this.len = len;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
