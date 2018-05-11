package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;

public class jump extends instruction{
    basicBlock jumpto;
    public jump(basicBlock itsBlock, basicBlock jumpto){
        this.itsBlock = itsBlock;
        this.jumpto = jumpto;
    }
}
