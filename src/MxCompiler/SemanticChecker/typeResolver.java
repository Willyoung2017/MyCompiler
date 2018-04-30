package MxCompiler.SemanticChecker;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Declaration.*;
import MxCompiler.Ast.Expression.PrimaryExpression.*;
import MxCompiler.Ast.Expression.SuffixExpression.*;
import MxCompiler.Ast.Statement.*;
import MxCompiler.Ast.TypeSpecifier.*;
import MxCompiler.Exception.compilationError;
import MxCompiler.Exception.semanticException;
import MxCompiler.Ast.Expression.BinaryExpression.binaryExpr;
import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.Ast.Expression.UnaryExpression.unaryExpr;
import MxCompiler.Ast.Expression.UnaryExpression.unaryOp;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.astNode;
import MxCompiler.Ast.abstractSyntaxTree;

public class typeResolver implements ASTVisitor {
    private MxCompiler.SemanticChecker.typeTable typeTable;
    public compilationError error;
    public classDec thisClass;
    public typeResolver(){
        typeTable = new typeTable();
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
            thisClass = node;
            node.type = new classType(node.name);
            node.classMems.stream().forEachOrdered(this::visit);
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
            if (node.functionType instanceof classType){
                typeTable.add(error,node.name, node);
            }
            if(node.functionType instanceof arrayType && ((arrayType)node.functionType).baseType instanceof classType)
                typeTable.add(error, node.name, (node.functionType));
            if (node.functionType != null){
                for (stmt statement: node.functionStmt.stmtList){
                    if(statement instanceof ifStmt){
                         if(((ifStmt)statement).ifBody instanceof returnStmt) {
                             exist = true;
                             if(!equalType(node.functionType, ((ifStmt)statement).ifBody.type)){
                                 error.add(new semanticException("Function's returnType is wrong!"+statement.loc.locString()));
                             }
                         }
                         else if (((ifStmt)statement).elseBody instanceof returnStmt){
                             exist = true;
                             if(!equalType(node.functionType, ((ifStmt)statement).elseBody.type)){
                                 error.add(new semanticException("Function's returnType is wrong!"+statement.loc.locString()));
                             }
                         }
                    }
                    if (statement instanceof returnStmt){
                        exist = true;
                        if(!equalType(node.functionType, ((returnStmt)statement).type)){
                            error.add(new semanticException("Function's returnType is wrong!"+statement.loc.locString()));
                        }
                    }
                }
               // if (!exist && !node.name.equals("main") && !(node.functionType instanceof voidType)) error.add(new semanticException("Function has no return!"+node.loc.locString()));

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
            if (node.variableType instanceof classType){
                typeTable.add(error,node.name, node);
            }
            if(node.variableType instanceof arrayType && getType((arrayType) node.variableType) instanceof classType) {
                astNode class_node = getType((arrayType) node.variableType);
                //typeTable.add(error, ((arrayType) node.variableType).baseType.name, node.variableType);
                astNode ent = node.scp.get(error, class_node.name, node.loc);
                if (ent != null) {
                    typeTable.typeMap.put(node.name, ent);
                }
            }
            if(node.variableExpression!= null && !equalType(node.variableType, node.variableExpression)){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            /*if (node.variableExpression != null
                    && (node.variableExpression.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof classType
                    && (!node.variableType.name.equals(node.variableExpression.type.name))){
                    error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if (node.variableExpression != null
                    && (node.variableExpression.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof arrayType
                    && (((arrayType) node.variableType).baseType.getClass() != ((arrayType)node.variableExpression.type).baseType.getClass())){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if(node.variableExpression != null && node.variableExpression.type.getClass() != node.variableType.getClass()){
                if (!(node.variableExpression.type instanceof nullType && node.variableType instanceof classType))
                    error.add(new semanticException("Variable type must be same!hhh"+node.loc.locString()));
            }*/
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
            node.type = node.variableType;
            if (node.variableType instanceof classType){
                typeTable.add(error,node.name, node);
            }
            if(node.variableType instanceof arrayType && getType((arrayType) node.variableType) instanceof classType) {
                astNode class_node = getType((arrayType) node.variableType);
                //typeTable.add(error, ((arrayType) node.variableType).baseType.name, node.variableType);
                astNode ent = node.scp.get(error, class_node.name, node.loc);
                if (ent != null) {
                    typeTable.typeMap.put(node.name, ent);
                }
            }

            if(node.variableExpression!= null && !equalType(node.variableType, node.variableExpression)){
                error.add(new semanticException("Variable type must be the same! "+node.loc.locString()));
            }
            /*if (node.variableExpression != null
                    && (node.variableExpression.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof classType
                    && !node.variableType.name.equals(node.variableExpression.type.name)){
                    error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if (node.variableExpression != null
                    && (node.variableExpression.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof arrayType
                    && (((arrayType) node.variableType).baseType.getClass() != ((arrayType)node.variableExpression.type).baseType.getClass())){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if(node.variableExpression != null && node.variableExpression.type.getClass() != node.variableType.getClass()){
                if (!(node.variableExpression.type instanceof nullType))
                    error.add(new semanticException("Variable type must be same!"+node.loc.locString()));
            }*/
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
            if (node.cond != null && !(node.cond.type instanceof boolType)){
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
            if (node.cond!= null && !(node.cond.type instanceof boolType)){
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
            if(node.returnExpr != null) {
                node.type = node.returnExpr.type;
            }
            else node.type = new voidType();
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
            if (node.variableType instanceof classType){
                typeTable.add(error,node.name, node);
            }
            if(node.variableType instanceof arrayType && getType((arrayType) node.variableType) instanceof classType) {
                astNode class_node = getType((arrayType) node.variableType);
                //typeTable.add(error, ((arrayType) node.variableType).baseType.name, node.variableType);
                astNode ent = node.scp.get(error, class_node.name, node.loc);
                if (ent != null) {
                    typeTable.typeMap.put(node.name, ent);
                }
            }

            if(node.variableExpr!=null && !equalType(node.variableType,node.variableExpr))
                error.add(new semanticException("Variable type must be the same !"+node.loc.locString()));
            /*if (node.variableExpr != null
                    && (node.variableExpr.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof classType
                    && !node.variableType.name.equals(node.variableExpr.type.name)){
                    error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if (node.variableExpr != null
                    && (node.variableExpr.type.getClass() == node.variableType.getClass())
                    && node.variableType instanceof arrayType
                    && (((arrayType) node.variableType).baseType.getClass() != ((arrayType)node.variableExpr.type).baseType.getClass())){
                error.add(new semanticException("Variable type must be the same!"+node.loc.locString()));
            }
            if(node.variableExpr != null && node.variableExpr.type.getClass() != node.variableType.getClass()){
                if (!((node.variableExpr.type instanceof nullType) &&(node.variableType instanceof classType)))
                    error.add(new semanticException("Variable type must be same!"+node.loc.locString()));
            }*/
        }
    }

    @Override
    public void visit(whileloopStmt node) {
        if (node != null){
            visit(node.cond);
            if (node.cond != null && !(node.cond.type instanceof boolType)){
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
            if(node.operator.equals(binaryOp.ADD)){
                if ((node.rightOperand.type instanceof intType && node.leftOperand.type instanceof intType)) {
                    node.type = new intType();
                }
                else if ((node.rightOperand.type instanceof stringType && node.leftOperand.type instanceof stringType)) {
                    node.type = new stringType();
                }
                else {
                    error.add(new semanticException("Operands error!"+node.loc.locString()));
                }
            }
            if(node.operator.equals(binaryOp.SUB)
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
            else if (node.operator.equals(binaryOp.LESS)
                    || node.operator.equals(binaryOp.LEQ)
                    || node.operator.equals(binaryOp.GREATER)
                    || node.operator.equals(binaryOp.GEQ)){
                if (!(node.leftOperand.type instanceof intType)){
                    error.add(new semanticException("Left Operand must be int type!"+node.leftOperand.loc.locString()));
                }
                if (!(node.rightOperand.type instanceof intType)) {
                    error.add(new semanticException("Right Operand must be int type" + node.loc.locString()));
                }
                node.type = new boolType();
            }
            else if(node.operator.equals(binaryOp.EQUAL)
                        || node.operator.equals(binaryOp.NOT_EQUAL)) {
                if (!equalType(node.leftOperand,node.rightOperand)
                        || (!(node.leftOperand.type instanceof stringType)
                        && !(node.leftOperand.type instanceof intType) && !(node.leftOperand.type instanceof classType) && !(node.leftOperand.type instanceof arrayType))) {
                    error.add(new semanticException("Operand is wrong"+node.loc.locString()));
                }
                node.type = new boolType();
            }
            else if (node.operator.equals(binaryOp.ASSIGN)){
                if(!equalType(node.leftOperand, node.rightOperand)){
                    error.add(new semanticException("Left and Right Operand of Assign must be the same!"+node.loc.locString()));
                }
                /*if ((node.rightOperand.type.getClass() != node.leftOperand.type.getClass())){
//                      if (node.rightOperand.)
                      //error.add(new semanticException("kkk"+node.leftOperand.type.toString()));
                      if (!(node.rightOperand.type instanceof nullType))
                         error.add(new semanticException("Left and Right Operand of Assign must be the same!"+node.loc.locString()));
                }
                if ((node.leftOperand.type.getClass() == node.rightOperand.type.getClass())
                        && node.leftOperand.type instanceof classType
                        && !node.leftOperand.type.name.equals(node.rightOperand.type.name)) {
                    error.add(new semanticException("Left and Right Operand of Assign must be the same!" + node.loc.locString()));
                }
                if ((node.leftOperand.type.getClass() == node.rightOperand.type.getClass())
                        && node.leftOperand.type instanceof arrayType
                        && (((arrayType) node.leftOperand.type).baseType.getClass() != ((arrayType)node.rightOperand.type).baseType.getClass())){
                    error.add(new semanticException("Left and Right Operand of Assign must be the same!"+node.loc.locString()));
                }*/
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
//        error.add(new semanticException("gg"+node.name));

        if (ent != null) {
 //           if(ent.type == null)
//            error.add(new semanticException("jjj"+ent.type.toString()+node.name));
            node.type = ent.type;
        }
        else node.type = new voidType();//2333
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
          //  error.add(new semanticException("jjjj"+node.type));
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
        if (node != null){
           visit(node.obj);
           boolean neijian = false;
           if (node.name.equals("size")){
                node.type = new intType();
                neijian = true;
           }
           else if (node.name.equals("length") || node.name.equals("parseInt")
                   || node.name.equals("ord")){
               node.type = new intType();
               neijian = true;
           }
           else if (node.name.equals("substring")){
               node.type = new stringType();
               neijian = true;
           }
           if(neijian) return;
            astNode ent;
            String className = node.obj.name;
            ent = typeTable.get(error, className, node);
            //error.add(new semanticException("gggg"+ent));
            if (ent == null){
                node.type = new voidType();
                return;
            }
            astNode memEnt = ent.scp.entities.get(node.name);
            if (memEnt == null) {
                error.add(new semanticException("The memberfunc accessed not found!" + node.loc.locString()));
                node.type = new voidType();
            } else {
                node.type = memEnt.type;
            }
        }
    }

    @Override
    public void visit(fieldmemAccessExpr node) {
        if (node != null) {
            visit(node.obj);
            String className = node.obj.name;
            if (node.obj instanceof funcCall){
                className = ((funcCall) node.obj).obj.name;
            }
            if (node.obj instanceof indexAccessExpr){
                className =((indexAccessExpr)(node.obj)).array.name;
           //     error.add(new semanticException("hhh"+className));
            }
            astNode ent;
            if(node.obj.name != null && node.obj.name.equals("this"))
                ent = thisClass;
            else {
               // if(!(node.obj instanceof indexAccessExpr))
                ent = typeTable.get(error, className, node);
                //error.add(new semanticException(className+"hhhhhh"));
                //error.add(new semanticException(className));
                //else {
                 //   ent = node.scp.get(error, className, node.loc);
                  //  ent = typeTable.get(error,ent.type.name,node);
                //}
               // error.add(new semanticException("ee"+className));
            }
            if (ent == null) {
                node.type = new voidType();
                return;
            }
            astNode memEnt = ent.scp.entities.get(node.name);
            if (memEnt == null) {
                error.add(new semanticException("The member accessed not found!" + node.loc.locString()));
                node.type = new voidType();
            } else {
                node.type = ((varDec)memEnt).variableType;
            }
            //error.add(new semanticException("hhh"+ent));
        }
    }

    @Override
    public void visit(funcCall node) {
        if (node != null){
            int i = 0;
            visit(node.obj);
            node.parameters.stream().forEachOrdered(this::visit);
            if (node.obj instanceof identifier){
                if (node.obj.name.equals("getInt")){
                    node.type = new intType();
                }
                else if (node.obj.name.equals("print") || node.obj.name.equals("println")){
                    node.type = new voidType();
                    if ((!(node.parameters.get(0).type instanceof stringType))){
                        error.add(new semanticException(node.obj.name + "Parameter is wrong" + node.loc.locString()));
                    }
                }
                else if (node.obj.name.equals("toString")){
                    node.type = new stringType();
                }
                else if (node.obj.name.equals("getString")){
                    node.type = new stringType();
                }
                else{
                if(!(((identifier) node.obj).ent instanceof funcDec)){
                    error.add(new semanticException("This is not a function!"+node.loc.locString()));
                }
                else {
                    if (((funcDec) ((identifier) node.obj).ent).parameterList.size() != node.parameters.size()) {
                        error.add(new semanticException("Parameters inconsistent!" + node.loc.locString()));
                    } else {
                        for (varDec para : ((funcDec) ((identifier) node.obj).ent).parameterList) {
                            expr funccall_para = node.parameters.get(i);
                            visit(funccall_para);
                            if (funccall_para.type.getClass() != para.variableType.getClass() && !(funccall_para.type instanceof nullType)) {
                                error.add(new semanticException("Parameter type is wrong" + node.loc.locString()));
                            }
                            ++i;
                        }
                    }
                    node.type = ((funcDec) ((identifier) node.obj).ent).functionType;
                }
                }
            }
        }
    }

    @Override
    public void visit(indexAccessExpr node) {
        if (node != null){
            visit(node.array);
            if((node.array.type instanceof arrayType)){
                node.type = ((arrayType)(node.array.type)).baseType;
            }
            else  error.add(new semanticException("This is not arraytype!"+node.loc.locString()));

            visit(node.index);
            if(!(node.index.type instanceof intType)){
                error.add(new semanticException("array index must be Inttype!"+node.loc.locString()));
            }
            node.name = node.array.name;
            //error.add(new semanticException("hhh"+node.type));
            /*if(node.array instanceof identifier) {
                node.name= node.array.name;
                astNode ent = node.scp.get(error, node.array.name, node.loc);
                if(ent != null){
                    node.type = ((arrayType)node.array.type).type;
                    //error.add(new semanticException(node.loc.locString()+node.type));
                }
                else {
                    node.type = new voidType();
                }
            }
            else {
                node.type = node.array.type;
                node.name = node.array.name;
            }*/
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
            if (node.operator.equals(unaryOp.LOGIC_NOT)) {
                node.type = new boolType();
                if (!(node.operand.type instanceof boolType)) {
                    error.add(new semanticException("The Operand must be booltype!" + node.loc.locString()));
                }
            }
            if (node.operator.equals(unaryOp.INCREMENT)
                    || node.operator.equals(unaryOp.DECREMENT)
                    || node.operator.equals(unaryOp.POS)
                    || node.operator.equals(unaryOp.NEG)
                    || node.operator.equals(unaryOp.BITWISE_NOT)){
                node.type = new intType();
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
            error.add(new semanticException("Array index must be inttype!"+node.loc.locString()+node.index.type.getClass().toString()));
        }
        if (node.baseType instanceof classType){
            astNode ent = node.scp.get(error,((classType)node.baseType).name,node.loc);
            if(ent != null)
                node.baseType = ent.type;
            else node.baseType = new voidType();
        }
        if(node.baseType instanceof arrayType){
            if(((arrayType)node.baseType).index == null && node.index != null){
                error.add(new semanticException("The index cannot be null!"+node.loc.locString()));
            }
        }
        node.type = node.baseType;
        node.name = node.baseType.name;
    }

    @Override
    public void visit(boolType node) {
        if(node != null){
            node.type = new boolType();
        }
    }

    @Override
    public void visit(classType node) {
        if(node != null){
            node.type = new classType(node.name);
        }
    }

    @Override
    public void visit(intType node) {
        if(node!=null){
            node.type = new intType();
        }
    }

    @Override
    public void visit(nullType node) {
        if(node != null){
            node.type = new nullType();
        }
    }

    @Override
    public void visit(stringType node) {
        if(node != null){
            node.type = new stringType();
        }
    }

    @Override
    public void visit(voidType node) {

    }

    private boolean equalType(expr A, expr B){
        if ((A.type instanceof arrayType || A.type instanceof classType) && B.type instanceof  nullType){
            return true;
        }
        if (A.type.getClass() == B.type.getClass()){
            if (A.type instanceof classType && !A.type.name.equals(B.type.name))
                return false;
            else if (A.type instanceof arrayType && !equalArrayType((arrayType) A.type, (arrayType)B.type))
                return false;
            else return true;
        }
        else return false;
    }

    private boolean equalType(typ A, expr B){
        if((A instanceof arrayType || A instanceof classType) && B.type instanceof nullType) {
            return true;
        }
        if (A.getClass() == B.type.getClass()){
            if (A instanceof classType && !A.name.equals(B.type.name))
                return false;
            else if (A instanceof arrayType && !equalArrayType((arrayType) A, (arrayType) B.type)){
                return false;
            }
            else return true;
        }
        else {
                return false;
        }
    }
    private boolean equalType(typ A, typ B){
        if((A instanceof arrayType || A instanceof classType) && B instanceof nullType) {
            return true;
        }
        if (A.getClass() == B.getClass()){
            if (A instanceof classType && !A.name.equals(B.name))
                return false;
            else if (A instanceof arrayType && !equalArrayType((arrayType) A, (arrayType) B)){
                return false;
            }
            else return true;
        }
        else {
            return false;
        }
    }
    private boolean equalArrayType(arrayType A, arrayType B){
        int dimA = 1, dimB = 1;
        typ baseType_A = A.baseType;
        typ baseType_B = B.baseType;
        while(baseType_A instanceof arrayType){
            ++dimA;
            baseType_A =((arrayType)baseType_A).baseType;
        }
        while(baseType_B instanceof arrayType){
            ++dimB;
            baseType_B = ((arrayType)baseType_B).baseType;
        }
        if(dimA != dimB || baseType_A.getClass() != baseType_B.getClass()){
            return false;
        }
        else return true;
    }

    private typ getType(arrayType A){
        typ baseType_A = A.baseType;
        while(baseType_A instanceof arrayType){
            baseType_A = ((arrayType)baseType_A).baseType;
        }
        return baseType_A;
    }
}
