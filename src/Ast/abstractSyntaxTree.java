package Ast;

import Ast.BuildAST.ASTVisitor;
import Ast.Declaration.dec;

import java.util.LinkedList;
import java.util.List;

public class abstractSyntaxTree extends astNode{
    public List<dec> declarations;

    public abstractSyntaxTree(){
        declarations = new LinkedList<>();
    }

    public abstractSyntaxTree(List<dec> declarations){
        this.declarations = declarations;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
