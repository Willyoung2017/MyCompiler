package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.virturalRegister;

public class move extends instruction {
    private virturalRegister destReg;
    private int address;

    public move(){
        destReg = null;
        address = -1;
    }

    public move(basicBlock itsBlock, virturalRegister destReg, int address){
        this.itsBlock = itsBlock;
        this.destReg = destReg;
        this.address = address;
    }
}
