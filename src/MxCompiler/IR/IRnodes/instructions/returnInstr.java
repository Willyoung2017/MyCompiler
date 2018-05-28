package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.Map;

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

    public void setUsedRegister(){
        usedRegister.clear();
        if(retReg instanceof register){
            usedRegister.add((register) retReg);
        }
    }

    public void setDefRegister(register reg){

    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(retReg instanceof virturalRegister){
            retReg = allocateMap.get(retReg);
        }
        setUsedRegister();
    }
}