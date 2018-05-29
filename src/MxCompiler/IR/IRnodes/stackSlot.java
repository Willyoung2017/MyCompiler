package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class stackSlot extends register{
    private String name;

    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
