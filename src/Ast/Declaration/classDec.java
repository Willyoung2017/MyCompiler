package Ast.Declaration;

import Ast.BuildAST.ASTVisitor;

import java.util.LinkedList;
import java.util.List;

public class classDec extends dec{
    //public String className;
    public List<memberDec> classMems;

    public classDec(){
        //className = null;
        classMems = new LinkedList<>();
    }

    public classDec(String claName, List<memberDec> claMems){
        name = claName;
        classMems = claMems;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
