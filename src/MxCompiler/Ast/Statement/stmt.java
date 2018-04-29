package MxCompiler.Ast.Statement;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
public class stmt extends astNode {

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
