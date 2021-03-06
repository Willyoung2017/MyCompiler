package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class move extends instruction {
    public register destReg;
    public intValue srcReg;

    public move(){
        destReg = null;
        srcReg = null;
    }

    public move(basicBlock itsBlock, intValue srcReg, register destReg){
        this.itsBlock = itsBlock;
        this.destReg = destReg;
        this.srcReg = srcReg;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return destReg;
    }

    public void setUsedRegister(){
        usedRegister.clear();
        if(srcReg instanceof register) usedRegister.add((register)srcReg);
    }

    public void setDefRegister(register reg){
        destReg = reg;
    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        if(srcReg instanceof virtualRegister){
            srcReg = allocateMap.get(srcReg);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
