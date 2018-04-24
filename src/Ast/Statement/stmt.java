package Ast.Statement;
import Ast.BuildAST.ASTVisitor;
import Ast.astNode;
public class stmt extends astNode {

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
