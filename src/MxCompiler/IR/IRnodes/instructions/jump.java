package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.Map;

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

    public void setDefRegister(register reg){

    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){

    }
}
