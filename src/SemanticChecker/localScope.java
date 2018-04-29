package SemanticChecker;

import Exception.compilationError;
import Ast.location;
import Ast.astNode;

public class localScope extends scope {
    protected  scope parent;

    public localScope(){
        parent = null;
    }

    public localScope(scope parent){
        this.parent = parent;
    }

    public astNode get(compilationError error, String name, location loc){
        astNode var = entities.get(name);
        if(var != null){
            return var;
        }
        else {
            return parent.get(error, name, loc);
        }
    }
}
