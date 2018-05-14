package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.address;
import MxCompiler.IR.IRnodes.intValue;

public class load extends instruction{
    public int offset;
    public int accessSize;
    public intValue dest;
    public intValue addr;

    public load(){
        dest = null;
        addr = null;
    }

    public load(intValue addr, intValue dest, int offset, int accessSize){
        this.addr = addr;
        this.dest = dest;
        this.offset = offset;
        this.accessSize = accessSize;
    }
}
