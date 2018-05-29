package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.Map;

public class heapAllocate extends instruction{
    public register destReg;
    public intValue allocSize;

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
        usedRegister.clear();
        if(allocSize instanceof register){
            usedRegister.add((register) allocSize);
        }
    }

    public void setDefRegister(register reg){
        destReg = reg;
    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(allocSize instanceof virturalRegister){
            allocSize = allocateMap.get(allocSize);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
