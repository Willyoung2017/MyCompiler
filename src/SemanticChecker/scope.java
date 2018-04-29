package SemanticChecker;

import Exception.compilationError;
import Exception.semanticException;
import Ast.astNode;
import Ast.location;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

abstract public class scope {
    protected List<scope> children;
    protected Map<String, astNode> entities;

    public scope(){
        children = new LinkedList<>();
        entities = new LinkedHashMap<>();
    }

    public scope(List<scope> children, Map<String, astNode> variables){
        this.children = children;
        this.entities = variables;
    }

    public void declareEntity(compilationError error, astNode var){
        astNode findVar = entities.get(var.getName());
        if(findVar != null){
            error.add(new semanticException("duplicated declaration: "+
                var.name+" at "+
                    var.loc.locString()+" and "+findVar.loc.locString()));
        }
        entities.put(var.name, var);
    }

    public abstract astNode get(compilationError error,String name, location loc);
}
