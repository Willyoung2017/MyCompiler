package MxCompiler.IR;

import MxCompiler.Ast.Declaration.classDec;
import MxCompiler.IR.symbol;

import java.util.HashMap;
import java.util.Map;

public class symbolTable {
    public Map<classDec, symbol> classMap;
    public Map<String, symbol> symbolMap;
    public symbolTable(){
        symbolMap = new HashMap<>();
    }
}
