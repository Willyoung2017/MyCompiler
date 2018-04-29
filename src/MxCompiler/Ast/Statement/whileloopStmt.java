package MxCompiler.Ast.Statement;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class whileloopStmt extends stmt{
    public expr cond;
    public stmt whileBody;

    public whileloopStmt(){
        cond = null;
        whileBody = null;
    }

    public whileloopStmt(expr condExpr, stmt whileBodyStmt){
        cond = condExpr;
        whileBody = whileBodyStmt;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
