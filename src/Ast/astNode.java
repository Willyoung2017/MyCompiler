package Ast;

import SemanticChecker.scope;

public class astNode {
    public location loc;
    public scope scp;
    public astNode(){
        loc = null;
        scp = null;
    }
}
