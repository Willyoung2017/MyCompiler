package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.*;

import java.util.Map;

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
        usedRegister.clear();
        if(srcReg instanceof register) usedRegister.add((register)srcReg);
    }

    public void setDefRegister(register reg){
        destReg = reg;
    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(srcReg instanceof  register){
            srcReg = allocateMap.get(srcReg);
        }
        setUsedRegister();
    }
}
