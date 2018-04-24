package Ast.Statement;

import Ast.BuildAST.ASTVisitor;

import java.util.LinkedList;
import java.util.List;

public class compoundStmt extends stmt {
    public List<stmt> stmtList;

    public compoundStmt(){

        stmtList = new LinkedList<>();
    }

    public compoundStmt(List<stmt> statementList){

        stmtList = statementList;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
