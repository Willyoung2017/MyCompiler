package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;

public class heapAllocate extends instruction{
    private register destReg;
    private intValue allocSize;

    public heapAllocate(register destReg, intValue allocSize){
        this.destReg = destReg;
        this.allocSize = allocSize;
    }
}
