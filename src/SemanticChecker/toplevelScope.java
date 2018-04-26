package SemanticChecker;

import Ast.astNode;
import Exception.*;
import java.util.List;
import java.util.Map;

public class toplevelScope extends scope{
    protected List<astNode> localVar;

    public astNode get(String name){
        astNode ent = entities.get(name);
        if(ent == null){
            compilationError.exceptionList.add(new semanticException("unresolved reference: "
                    + name));
        }
        return ent;
    }
}
