package MxCompiler.SemanticChecker;

import MxCompiler.Exception.compilationError;
import MxCompiler.Exception.semanticException;
import MxCompiler.Ast.astNode;
import MxCompiler.Ast.location;
import MxCompiler.SemanticChecker.scope;

import java.util.List;

public class toplevelScope extends scope {
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
