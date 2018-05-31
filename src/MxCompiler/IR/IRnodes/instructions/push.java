package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.Map;

public class push extends instruction{
    public register dest;

    public push(basicBlock itsBlock, register dest){
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
    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap) {

    }

    @Override
    public void setDefRegister(register reg) {

    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
