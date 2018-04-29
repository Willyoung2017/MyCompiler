package MxCompiler.Ast;

import MxCompiler.Ast.TypeSpecifier.typ;
import MxCompiler.SemanticChecker.scope;

public class astNode {
    public location loc;
    public scope scp;
    public String name;
    public typ type;

    public astNode(){
        loc = null;
        scp = null;
        name = null;
    }
    public String getName(){
        return name;
    }

    public void setScope(scope scp){
        this.scp = scp;
    }
}
