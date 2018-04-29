package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.TypeSpecifier.typ;

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
