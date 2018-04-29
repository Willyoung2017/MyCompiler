package MxCompiler.Ast.Declaration;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class memberDec extends dec{
    public dec declaration;

    public memberDec(){
        declaration = null;
    }

    public memberDec(dec declar){
        declaration = declar;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
