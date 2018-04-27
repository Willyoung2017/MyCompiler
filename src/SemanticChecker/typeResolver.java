package SemanticChecker;

import Ast.BuildAST.ASTVisitor;
import Ast.Declaration.*;
import Ast.Expression.BinaryExpression.binaryExpr;
import Ast.Expression.BinaryExpression.binaryOp;
import Ast.Expression.PrimaryExpression.*;
import Ast.Expression.SuffixExpression.*;
import Ast.Expression.UnaryExpression.unaryExpr;
import Ast.Expression.UnaryExpression.unaryOp;
import Ast.Expression.expr;
import Ast.astNode;
import Exception.*;
import Ast.Statement.*;
import Ast.TypeSpecifier.*;
import Ast.abstractSyntaxTree;

public class typeResolver implements ASTVisitor {
    private typeTable typeTable;
    public compilationError error;

    public typeResolver(){
        typeTable = null;
        error = new compilationError();
    }

    public typeResolver(typeTable typeTable, compilationError error){
        this.typeTable = typeTable;
        this.error = error;
    }

    @Override
    public void visit(abstractSyntaxTree node) {
        for (dec declaration: node.declarations){
            visit(declaration);
        }
    }

    @Override
    public void visit(dec node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(classDec node) {
        if (node != null){
            node.classMems.stream().forEachOrdered(this::visit);
            node.type = new classType(node.name);
        }
    }

    @Override
    public void visit(funcDec node) {
        if (node != null){
            boolean exist = false;
            visit(node.functionType);
            node.type = node.functionType;
            node.parameterList.stream().forEachOrdered(this::visit);
            visit(node.functionStmt);
            if (node.functionType != null){
                for (stmt statement: node.functionStmt.stmtList){
                    if (statement instanceof returnStmt){
                        exist = true;
                        if(statement.type.getClass() != node.functionType.getClass()){
                            error.add(new semanticException("Function's returnType is wrong!"+statement.loc.locString()));
                        }
                    }
                }
                if (!exist && !node.name.equals("main")) error.add(new semanticException("Function has no return!"+node.loc.locString()));

            }
        }
    }

    @Override
    public void visit(globalVarDec node) {
        if (node != null){
            if (node.variableType instanceof voidType){
                error.add(new semanticException("Variable cannot be VOID!"));
            }
            visit(node.variableType);
            visit(node.variableExpression);
            node.type = node.variableType;
            if (node.variableExpression != null && node.variableExpression.type.getClass() != node.variableType.getClass()){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
        }
    }

    @Override
    public void visit(constructFuncDec node) {
        if (node != null){
            node.parameters.stream().forEachOrdered(this::visit);
            visit(node.funcStmt);
        }
    }

    @Override
    public void visit(varDec node) {
        if (node != null){
            if (node.variableType instanceof voidType){
                error.add(new semanticException("Variable cannot be VOID!"));
            }
            visit(node.variableType);
            visit(node.variableExpression);
            if (node.variableExpression != null && node.variableExpression.type.getClass() != node.variableType.getClass()){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            node.type = node.variableType;
        }
    }

    @Override
    public void visit(memberDec node) {
        if (node != null){
            visit(node.declaration);
        }
    }

    @Override
    public void visit(stmt node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(breakStmt node) {

    }

    @Override
    public void visit(compoundStmt node) {
        if (node != null) {
            node.stmtList.stream().forEachOrdered(this::visit);
        }
    }

    @Override
    public void visit(continueStmt node) {

    }

    @Override
    public void visit(exprStmt node) {
        if (node != null){
            visit(node.expression);
        }
    }

    @Override
    public void visit(forloopStmt node) {
        if (node != null){
            visit(node.init);
            visit(node.cond);
            if (!(node.cond.type instanceof boolType)){
                error.add(new semanticException("For condition must be bool type!"+node.loc.locString()));
            }
            visit(node.step);
            visit(node.forBody);
        }

    }

    @Override
    public void visit(ifStmt node) {
        if (node != null){
            visit(node.cond);
            if (!(node.cond.type instanceof boolType)){
                error.add(new semanticException("If condition must be bool type!"+node.loc.locString()));
            }
            visit(node.ifBody);
            visit(node.elseBody);
        }
    }

    @Override
    public void visit(returnStmt node) {
        if (node != null){
            visit(node.returnExpr);
            node.type = node.returnExpr.type;
        }
    }

    @Override
    public void visit(varDecStmt node) {
        if (node != null){
            visit(node.variableType);
            visit(node.variableExpr);
            if (node.variableType instanceof voidType){
                error.add(new semanticException("Variable cannot be VOID!"));
            }
            node.type = node.variableType;
            if(node.variableExpr != null && node.variableExpr.type.getClass() != node.variableType.getClass()){
                error.add(new semanticException("Variable type must be same!"+node.loc.locString()));
            }
        }
    }

    @Override
    public void visit(whileloopStmt node) {
        if (node != null){
            visit(node.cond);
            if (!(node.cond.type instanceof boolType)){
                error.add(new semanticException("Whileloop condition must be bool type!"+node.loc.locString()));
            }
            visit(node.whileBody);
        }
    }

    @Override
    public void visit(expr node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryExpr node) {
        if (node != null) {
            visit(node.leftOperand);
            visit(node.rightOperand);
            if(node.operator.equals(binaryOp.ADD)
                    || node.operator.equals(binaryOp.SUB)
                    || node.operator.equals(binaryOp.MUL)
                    || node.operator.equals(binaryOp.DIV)
                    || node.operator.equals(binaryOp.MOD)
                    || node.operator.equals(binaryOp.BITWISE_INCLUSIVE_OR)
                    || node.operator.equals(binaryOp.BITWISE_EXCLUSIVE_OR)
                    || node.operator.equals(binaryOp.BITWISE_AND)
                    || node.operator.equals(binaryOp.LEFT_SHIFT)
                    || node.operator.equals(binaryOp.RIGHT_SHIFT)){
                if (!(node.leftOperand.type instanceof intType)){
                    error.add(new semanticException("Left Operand must be int type!"+node.leftOperand.loc.locString()));
                }
                if (!(node.rightOperand.type instanceof intType)) {
                    error.add(new semanticException("Right Operand must be int type" + node.loc.locString()));
                }
                node.type = new intType();
            }
            else if(node.operator.equals(binaryOp.LOGICAL_AND)
                    || node.operator.equals(binaryOp.LOGICAL_OR)){
                if (!(node.leftOperand.type instanceof boolType)){
                    error.add(new semanticException("Left Operand must be bool type!"+node.leftOperand.loc.locString()));
                }
                if (!(node.rightOperand.type instanceof boolType)){
                    error.add(new semanticException("Right operand must be bool type!"+node.rightOperand.loc.locString()));
                }
                node.type = new boolType();
            }
            else if (node.operator.equals(binaryOp.EQUAL)
                    || node.operator.equals(binaryOp.NOT_EQUAL)
                    || node.operator.equals(binaryOp.LESS)
                    || node.operator.equals(binaryOp.LEQ)
                    || node.operator.equals(binaryOp.GREATER)
                    || node.operator.equals(binaryOp.GEQ)){
                if (!(node.leftOperand.type instanceof intType)){
                    error.add(new semanticException("Left Operand must be int type!"+node.leftOperand.loc.locString()));
                }
                if (!(node.rightOperand.type instanceof intType)) {
                    error.add(new semanticException("Right Operand must be int type" + node.loc.locString()));
                }
                node.type = new intType();
            }
            else if (node.operator.equals(binaryOp.ASSIGN)){
                  if ((node.rightOperand.type.getClass() != node.leftOperand.type.getClass())){
                    error.add(new semanticException("Left and Right Operand of Assign must be the same!"+node.loc.locString()));
                }
                node.type = node.leftOperand.type;
            }
        }
    }

    @Override
    public void visit(primaryExpr node) {

    }

    @Override
    public void visit(boolConstant node) {
        if (node != null){
            node.type = new boolType();
        }
    }

    @Override
    public void visit(identifier node) {
        astNode ent = node.ent;
        if (ent != null) {
            node.type = ent.type;
        }
    }

    @Override
    public void visit(intConstant node) {
        if (node != null){
            node.type = new intType();
        }
    }

    @Override
    public void visit(newExpr node) {
        if (node != null){
            visit(node.newName);
            node.type = node.newName;
        }
    }

    @Override
    public void visit(Null node) {
        if (node != null){
            node.type = new nullType();
        }

    }

    @Override
    public void visit(stringConstant node) {
        if (node != null){
            node.type = new stringType();
        }
    }

    @Override
    public void visit(fieldfuncAccessExpr node) {

    }

    @Override
    public void visit(fieldmemAccessExpr node) {

    }

    @Override
    public void visit(funcCall node) {
        if (node != null){
            int i = 0;
            visit(node.obj);
            node.parameters.stream().forEachOrdered(this::visit);
            if (node.obj instanceof identifier){
                if(((funcDec) ((identifier) node.obj).ent).parameterList.size() != node.parameters.size()){
                    error.add(new semanticException("Parameters Lost!"+node.loc.locString()));
                }
                else {
                    for (varDec para : ((funcDec) ((identifier) node.obj).ent).parameterList) {
                        expr funccall_para = node.parameters.get(i);
                        if (funccall_para.type.toString() != para.variableType.toString()){
                            error.add(new semanticException("Parameter type is wrong"+node.loc.locString()));
                        }
                        ++i;
                    }
                }
                node.type = ((funcDec) ((identifier) node.obj).ent).functionType;
            }
        }
    }

    @Override
    public void visit(indexAccessExpr node) {
        if (node != null){
            visit(node.array);
            if(!(node.array.type instanceof arrayType)){
                error.add(new semanticException("This is not arraytype!"+node.loc.locString()));
            }
            visit(node.index);
            if(!(node.index.type instanceof intType)){
                error.add(new semanticException("array index must be Inttype!"+node.loc.locString()));
            }
            node.type = node.array.type;
        }
    }

    @Override
    public void visit(selfDec node) {
        if (node != null){
            visit(node.operand);
            if (!(node.operand.type instanceof intType)) {
                error.add(new semanticException("selfDec must be Inttype!"+node.loc.locString()));
            }
            node.type = new intType();
        }
    }

    @Override
    public void visit(selfInc node) {
        if (node != null){
            visit(node.operand);
            if (!(node.operand.type instanceof intType)) {
                error.add(new semanticException("selfInc must be Inttype!"+node.loc.locString()));
            }
            node.type = new intType();
        }
    }

    @Override
    public void visit(suffixExpr node) {
        if (node != null){
            visit(node.operand);
        }
    }

    @Override
    public void visit(unaryExpr node) {
        if (node != null){
            visit(node.operand);
            if (node.operator.equals(unaryOp.LOGIC_NOT) && !(node.operand.type instanceof boolType)){
                error.add(new semanticException("The Operand must be booltype!"+node.loc.locString()));
            }
            if (node.operator.equals(unaryOp.INCREMENT)
                    || node.operator.equals(unaryOp.DECREMENT)
                    || node.operator.equals(unaryOp.POS)
                    || node.operator.equals(unaryOp.NEG)
                    || node.operator.equals(unaryOp.BITWISE_NOT)){
                if(!(node.operand.type instanceof intType)) {
                    error.add(new semanticException("The Operand must be inttype!" + node.loc.locString()));
                }
            }
        }
    }

    @Override
    public void visit(typ node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(arrayType node) {
        if (node == null) return;
        visit(node.baseType);
        if (node.baseType instanceof voidType){
            error.add(new semanticException("Variable cannot be VOID!"+node.loc.locString()));
        }
        visit(node.index);
        if (node.index != null && (!(node.index.type instanceof intType))){
            error.add(new semanticException("Array index must be inttype!"+node.loc.locString()));
        }
    }

    @Override
    public void visit(boolType node) {

    }

    @Override
    public void visit(classType node) {

    }

    @Override
    public void visit(intType node) {

    }

    @Override
    public void visit(nullType node) {

    }

    @Override
    public void visit(stringType node) {

    }

    @Override
    public void visit(voidType node) {

    }
}
