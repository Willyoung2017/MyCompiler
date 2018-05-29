package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class store extends instruction{
    public int offset;
    public int accessSize;
    public intValue src;
    public intValue addr;

    public store(){
        src = null;
        addr = null;
    }

    public store(basicBlock itsBlock, intValue src, intValue addr, int offset, int accessSize){
        this.itsBlock = itsBlock;
        this.src = src;
        this.addr = addr;
        this.offset = offset;
        this.accessSize = accessSize;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    public void setUsedRegister(){
        usedRegister.clear();
        if(src instanceof register) usedRegister.add((register) src);
    }


    public void setDefRegister(register reg){

    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(src instanceof  virturalRegister){
            src = allocateMap.get(src);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
