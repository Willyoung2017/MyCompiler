package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class Null extends expr {

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
