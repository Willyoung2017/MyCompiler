package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.address;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;

public class store extends instruction{
    public int offset;
    public int accessSize;
    public intValue src;
    public intValue addr;

    public store(){
        src = null;
        addr = null;
    }

    public store(intValue src, intValue addr, int offset, int accessSize){
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
        if(src instanceof register) usedRegister.add((register) src);
    }
}
