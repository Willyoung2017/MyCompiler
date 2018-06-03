package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class pop extends instruction{
    public intValue dest;

    public pop(basicBlock itsBlock, intValue dest){
        this.itsBlock = itsBlock;
        this.dest = dest;
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() {

    }

    @Override
    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap) {

    }

    @Override
    public void setDefRegister(register reg) {

    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
