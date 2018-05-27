package MxCompiler.BackEnd;

import MxCompiler.IR.IRnodes.func;

import java.util.Map;

public class registerAllocator {
    private Map<String, func> funcMap;

    public registerAllocator(Map<String, func> funcMap){
        this.funcMap = funcMap;
    }

    public void allocateRegister(){

    }
}
