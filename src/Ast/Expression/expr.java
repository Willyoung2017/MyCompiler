package Ast.Expression;
import Ast.BuildAST.ASTVisitor;
import Ast.TypeSpecifier.typ;
import Ast.astNode;

public class expr extends astNode{
    public typ exprType;
    public boolean isLvalue;

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
