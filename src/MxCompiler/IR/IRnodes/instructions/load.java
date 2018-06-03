package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class load extends instruction{
    public int offset;
    public int accessSize;
    public register dest;
    public intValue addr;

    public load(){
        dest = null;
        addr = null;
    }

    public load(basicBlock itsBlock, intValue addr, register dest, int offset, int accessSize){
        this.itsBlock = itsBlock;
        this.addr = addr;
        this.dest = dest;
        this.offset = offset;
        this.accessSize = accessSize;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return dest;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if(addr instanceof register) usedRegister.add((register) addr);
    }

    public void setDefRegister(register reg){
        dest = reg;
    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        if(addr instanceof virtualRegister){
            addr = allocateMap.get(addr);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
