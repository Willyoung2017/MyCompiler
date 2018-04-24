package Ast.TypeSpecifier;
import Ast.BuildAST.ASTVisitor;
import Ast.astNode;
public class typ extends astNode{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
