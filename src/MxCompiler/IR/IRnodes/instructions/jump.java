package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virtualRegister;

import java.util.Map;

public class jump extends instruction{
    public basicBlock jumpto;

    public jump(basicBlock itsBlock, basicBlock jumpto){
        this.itsBlock = itsBlock;
        this.jumpto = jumpto;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    public void setUsedRegister(){

    }

    public void setDefRegister(register reg){

    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){

    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
