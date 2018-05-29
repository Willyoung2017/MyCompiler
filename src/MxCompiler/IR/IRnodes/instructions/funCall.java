package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class funCall extends instruction{
    public register returnReg;
    public func function;
    public List<intValue> parameters = new LinkedList<>();
    public funCall(){
        returnReg = null;
        function = null;
    }

    public funCall(register returnReg, func function){
        this.returnReg = returnReg;
        this.function = function;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return returnReg;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        for(intValue para : parameters){
            if(para instanceof register){
                usedRegister.add((register) para);
            }
        }
    }

    public void setDefRegister(register reg){
        returnReg = reg;
    }

    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        for(intValue para : parameters){
            if(para instanceof virturalRegister){
                para = allocateMap.get(para);
            }
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
