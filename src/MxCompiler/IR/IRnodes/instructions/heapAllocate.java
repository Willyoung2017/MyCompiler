package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;

public class heapAllocate extends instruction{
    private register destReg;
    private intValue allocSize;

    public heapAllocate(register destReg, intValue allocSize){
        this.destReg = destReg;
        this.allocSize = allocSize;
        setUsedRegister();
    }

    public register getDefRegister(){
        return destReg;
    }

    @Override
    public void setUsedRegister() {
        if(allocSize instanceof register){
            usedRegister.add((register) allocSize);
        }
    }
}
