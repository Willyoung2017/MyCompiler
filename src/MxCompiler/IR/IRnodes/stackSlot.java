package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class stackSlot extends register{
    private String name;
    private func function;

    public stackSlot(String name, func function){
        this.name = name;
        this.function = function;
    }
    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
