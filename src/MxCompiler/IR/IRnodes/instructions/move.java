package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

public class move extends instruction {
    public register destReg;
    public intValue srcReg;

    public move(){
        destReg = null;
        srcReg = null;
    }

    public move(intValue srcReg, register destReg){
        this.destReg = destReg;
        this.srcReg = srcReg;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return destReg;
    }

    public void setUsedRegister(){
        if(srcReg instanceof register) usedRegister.add((register)srcReg);
    }
}
