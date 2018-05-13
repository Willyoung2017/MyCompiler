package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.virturalRegister;

public class move extends instruction {
    public virturalRegister destReg;
    public intValue srcReg;

    public move(){
        destReg = null;
        srcReg = null;
    }

    public move( intValue srcReg,virturalRegister destReg){
        this.destReg = destReg;
        this.srcReg = srcReg;
    }
}
