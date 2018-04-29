package Ast.Expression;
import Ast.BuildAST.ASTVisitor;
import Ast.astNode;

public class expr extends astNode{
    public boolean isLvalue;

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
