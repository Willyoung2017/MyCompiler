package SemanticChecker;

import Exception.compilationError;
import Exception.semanticException;
import Ast.astNode;
import Ast.location;

import java.util.List;

public class toplevelScope extends scope{
    protected List<astNode> localVar;

    public astNode get(compilationError error, String name, location loc){
        astNode ent = entities.get(name);
        if(ent == null){
            error.add(new semanticException("unresolved reference: "
                    + name + " at " + loc.locString()));
        }
        return ent;
    }
}
