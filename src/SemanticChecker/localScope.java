package SemanticChecker;

import Ast.astNode;

import java.util.Map;

public class localScope extends scope {
    protected  scope parent;
    protected Map<String, astNode> variables;
}
