package Ast.Statement;

import Ast.BuildAST.ASTVisitor;

public class continueStmt extends stmt{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
