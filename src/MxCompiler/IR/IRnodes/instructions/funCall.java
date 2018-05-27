package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.register;

import java.util.LinkedList;
import java.util.List;

public class funCall extends instruction{
    public register returnReg;
    public func function;
    public List<intValue> parameters;
    public funCall(){
        returnReg = null;
        function = null;
        parameters = new LinkedList<>();
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
        for(intValue para : parameters){
            if(para instanceof register){
                usedRegister.add((register) para);
            }
        }
    }
}
