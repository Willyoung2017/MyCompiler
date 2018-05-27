package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;

public class returnInstr extends instruction{
    public intValue retReg;

    public returnInstr(){
        retReg = null;
    }

    public returnInstr(intValue retReg){
        this.retReg = retReg;
    }

    @Override
    public register getDefRegister() {
        return null;
    }
}
