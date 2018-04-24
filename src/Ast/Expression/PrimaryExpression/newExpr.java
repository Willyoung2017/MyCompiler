package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;
import Ast.TypeSpecifier.typ;

public class newExpr extends expr {
    public typ newName;

    public newExpr(){
        newName = null;
    }

    public newExpr(typ newName){
        this.newName = newName;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
