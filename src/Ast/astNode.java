package Ast;

import SemanticChecker.scope;

public class astNode {
    public location loc;
    public scope scp;
    public String name;
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
