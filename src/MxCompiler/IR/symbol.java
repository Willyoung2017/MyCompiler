package MxCompiler.IR;

import MxCompiler.Ast.Declaration.classDec;
import MxCompiler.Ast.Declaration.constructFuncDec;
import MxCompiler.Ast.Declaration.dec;
import MxCompiler.Ast.Declaration.funcDec;

import java.util.HashMap;
import java.util.Map;

public class symbol {
    public int size = 0;
    public Map<String, Integer> offsetMap = new HashMap<>();
    public Map<String, Integer> sizeMap = new HashMap<>();

    public void setSymbol(classDec node){
        int offset = 0;
        for(dec mem: node.classMems){
            if(!(mem instanceof funcDec) && !(mem instanceof constructFuncDec)){
                size += mem.type.size;
                offsetMap.put(mem.name, offset);
                sizeMap.put(mem.name, mem.type.size);
                offset = size;
            }
        }
    }
}
