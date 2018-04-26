package SemanticChecker;

import Ast.astNode;
import Exception.*;
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

    public void declareEntity(astNode var){
        astNode findVar = entities.get(var.getName());
        if(findVar != null){
            compilationError.exceptionList.add(new semanticException("duplicated declaration: "+
                var.name+": "+
                    var.loc.locString()+" and "+var.loc.locString()));
        }
        entities.put(var.name, var);
    }

    public abstract astNode get(String name);
}
