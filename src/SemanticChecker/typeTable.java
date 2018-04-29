package SemanticChecker;

import Exception.compilationError;
import Exception.semanticException;
import Ast.TypeSpecifier.classType;
import Ast.astNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class typeTable {
    public Map<String, astNode> typeMap;

    public typeTable(){
        typeMap = new LinkedHashMap<>();
    }

    public void add(compilationError error, String name, astNode node){
//        if(node.scp == null) error.add(new semanticException("hhhh"));
        astNode ent = node.scp.get(error, ((classType) node.type).name, node.loc);
        if (ent != null) {
            typeMap.put(name, ent);
        }
    }

    public astNode get(compilationError error, String name, astNode node){
        astNode ent = typeMap.get(name);
        if (ent == null){
            error.add(new semanticException("Class not found!"+node.loc.locString()));
        }
        return ent;
    }
}
