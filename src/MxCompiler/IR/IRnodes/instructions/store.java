package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;

public class store extends instruction{
    public int offset;
    public intValue src;
    public intValue addr;

    public store(){
        src = null;
        addr = null;
    }

    public store(intValue src, intValue addr, int offset){
        this.src = src;
        this.addr = addr;
        this.offset = offset;
    }
}
