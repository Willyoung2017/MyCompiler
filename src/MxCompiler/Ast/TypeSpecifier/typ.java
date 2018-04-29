package MxCompiler.Ast.TypeSpecifier;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
public class typ extends astNode{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
