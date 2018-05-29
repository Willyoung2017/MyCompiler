package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class physicRegister extends register{
    private String name;

    public physicRegister(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
