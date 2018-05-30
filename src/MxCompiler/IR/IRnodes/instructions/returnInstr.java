package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class returnInstr extends instruction{
    public intValue retReg;

    public returnInstr(){
        retReg = null;
    }

    public returnInstr(basicBlock itsBlock, intValue retReg){
        this.itsBlock = itsBlock;
        this.retReg = retReg;
        setUsedRegister();
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

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
