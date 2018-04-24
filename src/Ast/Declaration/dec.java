package Ast.Declaration;
import Ast.BuildAST.ASTVisitor;
import Ast.astNode;
public class dec extends astNode{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
