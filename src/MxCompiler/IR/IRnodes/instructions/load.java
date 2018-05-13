package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;

public class load extends instruction{
    public int offset;
    public intValue src;
    public intValue addr;

    public load(){
        src = null;
        addr = null;
    }
}
