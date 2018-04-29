package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.astNode;

public class identifier extends expr {
    //public String name;
    public astNode ent;

    public identifier(){
        name = null;
    }

    public identifier(String name){
        this.name = name;
    }

    public void setEnt(astNode ent){
        this.ent = ent;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
