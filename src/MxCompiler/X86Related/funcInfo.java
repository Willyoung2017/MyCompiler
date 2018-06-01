package MxCompiler.X86Related;

import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.stackSlot;

import java.util.HashMap;
import java.util.Map;

public class funcInfo {
    private func function;
    public int returnAddr;
    public int firstParaAddr;
    public int lastParaAddr;
    public int calleeSaveAddr;
    public int callerSaveAddr;
    public int localVarAddr;
    public int totalSize;
    public Map<stackSlot, Integer> stackSlotOffsetMap = new HashMap<>();

    public funcInfo(func function){
        this.function = function;
        function.info = this;
    }

    public void setOffset(){
        returnAddr = 8;
        firstParaAddr = -8;
        int paraSize = function.parameterList.size();
        lastParaAddr = paraSize > 6 ? (paraSize - 6) * 8 + returnAddr : returnAddr;
        calleeSaveAddr = paraSize > 6 ? -56 : -8 * (paraSize + 1);
        localVarAddr = calleeSaveAddr - 8 * function.usedCalleeSaved.size();
        callerSaveAddr = localVarAddr - 8 * function.varItemList.size();
        totalSize = -callerSaveAddr + 8 * function.usedCallerSaved.size();
    }

    public void setoffsetMap(){
        for(int i = 0; i < function.varItemList.size(); ++i){
            stackSlotOffsetMap.put(function.varItemList.get(i), localVarAddr - 8 * i);
        }
        for(int i = 0; i < function.parameterList.size(); ++i){
            if(i <= 5){
                stackSlotOffsetMap.put((stackSlot) function.parameterList.get(i), firstParaAddr - 8 * i);
            }
            else{
                stackSlotOffsetMap.put((stackSlot) function.parameterList.get(i), lastParaAddr - 8 * (function.parameterList.size()-i-1));
            }
        }
    }
}
