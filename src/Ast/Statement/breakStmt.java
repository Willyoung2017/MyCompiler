package Ast.Statement;

import Ast.BuildAST.ASTVisitor;

public class breakStmt extends stmt{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
