package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.register;

public class jump extends instruction{
    public basicBlock jumpto;

    public jump(basicBlock jumpto){
        this.jumpto = jumpto;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    public void setUsedRegister(){

    }
}
