package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;

public class jump extends instruction{
    basicBlock jumpto;
    public jump(basicBlock jumpto){
        this.jumpto = jumpto;
    }
}
