package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class heapAllocate extends instruction{
    public register destReg;
    public intValue allocSize;

    public heapAllocate(basicBlock itsBlock, register destReg, intValue allocSize){
        this.itsBlock = itsBlock;
        this.destReg = destReg;
        this.allocSize = allocSize;
        setUsedRegister();
    }

    public register getDefRegister(){
        return destReg;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if(allocSize instanceof register){
            usedRegister.add((register) allocSize);
        }
    }

    public void setDefRegister(register reg){
        destReg = reg;
    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        if(allocSize instanceof virtualRegister){
            allocSize = allocateMap.get(allocSize);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
