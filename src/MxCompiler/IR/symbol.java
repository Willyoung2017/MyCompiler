package MxCompiler.IR;

import MxCompiler.Ast.Declaration.*;

import java.util.HashMap;
import java.util.Map;

public class symbol {
    public int size = 0;
    boolean hasConstructFunc = false;
    public Map<String, Integer> offsetMap = new HashMap<>();
    public Map<String, Integer> sizeMap = new HashMap<>();

    public void setSymbol(classDec node){
        int offset = 0;
        for(memberDec mem: node.classMems){
            dec memDec = mem.declaration;
            if(!(memDec instanceof funcDec) && !(memDec instanceof constructFuncDec)){
                size += memDec.type.size;
                offsetMap.put(memDec.name, offset);
                sizeMap.put(memDec.name, memDec.type.size);
                offset = size;
            }
            else if(memDec instanceof constructFuncDec){
                hasConstructFunc = true;
            }
        }
    }
}
