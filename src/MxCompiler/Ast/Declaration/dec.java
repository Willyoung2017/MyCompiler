package MxCompiler.Ast.Declaration;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
public class dec extends astNode{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
