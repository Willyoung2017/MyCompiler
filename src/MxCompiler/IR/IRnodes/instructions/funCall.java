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

    public funCall(basicBlock itsBlock, register returnReg, func function){
        this.itsBlock = itsBlock;
        this.returnReg = returnReg;
        this.function = function;
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

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        for(int i = 0; i < parameters.size(); ++i){
            intValue para = parameters.get(i);
            if(para instanceof virtualRegister){
                parameters.set(i, allocateMap.get(para));
            }
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
