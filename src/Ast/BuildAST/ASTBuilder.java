package Ast.BuildAST;

import Ast.Expression.BinaryExpression.binaryExpr;
import Ast.Expression.BinaryExpression.binaryOp;
import Ast.Expression.PrimaryExpression.*;
import Ast.Expression.SuffixExpression.*;
import Ast.Expression.UnaryExpression.unaryExpr;
import Ast.Expression.UnaryExpression.unaryOp;
import Ast.Statement.*;
import Ast.abstractSyntaxTree;
import Ast.astNode;
import Ast.location;
import Parser.MxLexer;
import Parser.MxParser;
import com.sun.jdi.BooleanType;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import Parser.MxBaseListener;
import Ast.Declaration.*;
import Ast.TypeSpecifier.*;
import Ast.Expression.*;

import java.math.BigInteger;

public class ASTBuilder extends MxBaseListener {
    public MxParser parser;
    public ParseTreeProperty<astNode> property = new ParseTreeProperty<>();
    public abstractSyntaxTree rootNode;

    public ASTBuilder(MxParser parser) {
        this.parser = parser;
    }

    public abstractSyntaxTree getRootNode(){return rootNode;}

    @Override
    public void exitProgram(MxParser.ProgramContext ctx){
        super.exitProgram(ctx);
        abstractSyntaxTree treeNode = new abstractSyntaxTree();
        rootNode = treeNode;
        property.put(ctx, treeNode);
        for (MxParser.ExternalDeclarationContext p : ctx.externalDeclaration()){
            astNode node = property.get(p);
            treeNode.declarations.add(((dec) node));
        }
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitExternalDeclaration(MxParser.ExternalDeclarationContext ctx) {
        super.exitExternalDeclaration(ctx);
        if (ctx.classDeclaration() != null){
            property.put(ctx, property.get(ctx.classDeclaration()));
        }
        else if (ctx.functionDeclaration() != null){
            property.put(ctx, property.get(ctx.functionDeclaration()));
        }
        else if (ctx.globalVariableDeclaration() != null){
            property.put(ctx, property.get(ctx.globalVariableDeclaration()));
        }
    }

    @Override
    public void exitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        super.exitClassDeclaration(ctx);
        classDec treeNode = new classDec();
        property.put(ctx, treeNode);
        for (MxParser.MemberDeclarationContext p : ctx.memberDeclaration()){
            astNode node = property.get(p);
            treeNode.classMems.add((memberDec) node);
        }
        treeNode.name = ctx.Identifier().getText();
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx){
        super.exitFunctionDeclaration(ctx);
        funcDec treeNode = new funcDec();
        property.put(ctx, treeNode);

        treeNode.functionType = (typ) property.get(ctx.typeSpecifier());
        treeNode.name = ctx.Identifier().getText();
        treeNode.functionStmt = (compoundStmt) property.get(ctx.compoundStatement());
        for (MxParser.VariableDeclarationContext p : ctx.variableDeclaration()){
            astNode node = property.get(p);
            treeNode.parameterList.add((varDec) node);
        }
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitGlobalVariableDeclaration(MxParser.GlobalVariableDeclarationContext ctx) {
        super.exitGlobalVariableDeclaration(ctx);
        globalVarDec treeNode = new globalVarDec();
        property.put(ctx, treeNode);

        treeNode.name = ctx.Identifier().getText();
        treeNode.variableType = (typ) property.get(ctx.typeSpecifier());
        treeNode.variableExpression = (expr) property.get(ctx.expression());
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

    }

    @Override
    public void exitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        super.exitVariableDeclaration(ctx);
        varDec treeNode = new varDec();
        property.put(ctx, treeNode);

        treeNode.name = ctx.Identifier().getText();
        treeNode.variableType = (typ) property.get(ctx.typeSpecifier());
        treeNode.variableExpression = (expr) property.get(ctx.expression());
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitConstructFunctionDeclaration(MxParser.ConstructFunctionDeclarationContext ctx) {
        super.exitConstructFunctionDeclaration(ctx);
        constructFuncDec treeNode = new constructFuncDec();
        property.put(ctx, treeNode);

        treeNode.name = ctx.Identifier().getText();
        treeNode.funcStmt = (compoundStmt) property.get(ctx.compoundStatement());
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitMemberDeclaration(MxParser.MemberDeclarationContext ctx) {
        super.exitMemberDeclaration(ctx);
        memberDec treeNode = new memberDec();
        property.put(ctx, treeNode);
        if (ctx.functionDeclaration() != null) {
            treeNode.declaration = (funcDec) property.get(ctx.functionDeclaration());
        }
        else if (ctx.constructFunctionDeclaration() != null){
            treeNode.declaration = (constructFuncDec) property.get(ctx.constructFunctionDeclaration());
        }
        else if (ctx.variableDeclaration() != null){
            treeNode.declaration = (varDec) property.get(ctx.variableDeclaration());
        }
    }

    @Override
    public void exitStatement(MxParser.StatementContext ctx) {
        super.exitStatement(ctx);
        if (ctx.compoundStatement() != null){
            property.put(ctx, property.get(ctx.compoundStatement()));
        }
        else if (ctx.expressionStatement() != null){
            property.put(ctx, property.get(ctx.expressionStatement()));
        }
        else if (ctx.selectionStatement() != null){
            property.put(ctx, property.get(ctx.selectionStatement()));
        }
        else if (ctx.jumpStatement() != null){
            property.put(ctx, property.get(ctx.jumpStatement()));
        }
        else if (ctx.iterationStatement() != null){
            property.put(ctx, property.get(ctx.iterationStatement()));
        }
        else if (ctx.variableDeclarationStatement() != null){
            property.put(ctx, property.get(ctx.variableDeclarationStatement()));
        }
    }

    @Override
    public void exitCompoundStatement(MxParser.CompoundStatementContext ctx) {
        super.exitCompoundStatement(ctx);
        compoundStmt treeNode = new compoundStmt();
        property.put(ctx, treeNode);
        for (MxParser.StatementContext p : ctx.statement()){
            treeNode.stmtList.add(((stmt) property.get(p)));
        }
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitExpressionStatement(MxParser.ExpressionStatementContext ctx) {
        super.exitExpressionStatement(ctx);
        exprStmt treeNode = new exprStmt();
        property.put(ctx, treeNode);
        treeNode.expression = (expr) property.get(ctx.expression());
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitSelectionStatement(MxParser.SelectionStatementContext ctx) {
        super.exitSelectionStatement(ctx);
        ifStmt treeNode = new ifStmt();
        property.put(ctx, treeNode);
        treeNode.cond = (expr) property.get(ctx.expression());
        treeNode.ifBody = (stmt) property.get(ctx.statement(0));
        if (ctx.Else() != null){
            treeNode.elseBody = (stmt) property.get(ctx.statement(1));
        }
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitIterationStatement(MxParser.IterationStatementContext ctx) {
        super.exitIterationStatement(ctx);
        if (ctx.For() != null){
            forloopStmt treeNode = new forloopStmt();
            property.put(ctx, treeNode);
            if (ctx.init() != null) {
                treeNode.init = (expr) property.get(ctx.init());
            }
            if (ctx.step() != null) {
                treeNode.step = (expr) property.get(ctx.step());
            }
            if (ctx.condition() != null){
                treeNode.cond = (expr) property.get(ctx.condition());
            }
            treeNode.forBody = (stmt) property.get(ctx.statement());
        }
        else if (ctx.While() != null){
            whileloopStmt treeNode = new whileloopStmt();
            property.put(ctx, treeNode);
            if (ctx.expression() != null) {
                treeNode.cond = (expr) property.get(ctx.expression());
            }
            treeNode.whileBody = (stmt) property.get(ctx.statement());
        }
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitJumpStatement(MxParser.JumpStatementContext ctx) {
        super.exitJumpStatement(ctx);
        if (ctx.Break() != null) {
            breakStmt treeNode = new breakStmt();
            property.put(ctx, treeNode);

        }
        else if (ctx.Continue() != null){
            continueStmt treeNode = new continueStmt();
            property.put(ctx, treeNode);

        }
        else if (ctx.Return() != null){
            returnStmt treeNode = new returnStmt();
            property.put(ctx, treeNode);
            treeNode.returnExpr = (expr) property.get(ctx.expression());
        }

        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

    }

    @Override
    public void exitVariableDeclarationStatement(MxParser.VariableDeclarationStatementContext ctx) {
        super.exitVariableDeclarationStatement(ctx);
        varDecStmt treeNode = new varDecStmt();
        property.put(ctx, treeNode);
        treeNode.variableType = (typ) property.get(ctx.typeSpecifier());
        treeNode.name = ctx.Identifier().getText();
        treeNode.variableExpr = (expr) property.get(ctx.expression());
        property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

    }

    @Override
    public void exitExpression(MxParser.ExpressionContext ctx) {
        super.exitExpression(ctx);
        property.put(ctx, property.get(ctx.assignmentExpression()));

    }

    @Override
    public void exitAssignmentExpression(MxParser.AssignmentExpressionContext ctx) {
        super.exitAssignmentExpression(ctx);
        if (ctx.unaryExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.operator = binaryOp.ASSIGN;
            treeNode.leftOperand = (expr) property.get(ctx.unaryExpression());
            treeNode.rightOperand = (expr) property.get(ctx.assignmentExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        }
        else {
            property.put(ctx, property.get(ctx.logicalOrExpression()));
        }
    }

    @Override
    public void exitLogicalOrExpression(MxParser.LogicalOrExpressionContext ctx) {
        super.exitLogicalOrExpression(ctx);
        if (ctx.logicalOrExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.operator = binaryOp.LOGICAL_OR;
            treeNode.leftOperand = (expr) property.get(ctx.logicalOrExpression());
            treeNode.rightOperand = (expr) property.get(ctx.logicalAndExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.logicalAndExpression()));
        }
    }

    @Override
    public void exitLogicalAndExpression(MxParser.LogicalAndExpressionContext ctx) {
        super.exitLogicalAndExpression(ctx);
        if (ctx.logicalAndExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.leftOperand = (expr) property.get(ctx.logicalAndExpression());
            treeNode.rightOperand = (expr) property.get(ctx.bitwiseInclusiveOrExpression());
            treeNode.operator = binaryOp.LOGICAL_AND;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.bitwiseInclusiveOrExpression()));
        }
    }

    @Override
    public void exitBitwiseInclusiveOrExpression(MxParser.BitwiseInclusiveOrExpressionContext ctx) {
        super.exitBitwiseInclusiveOrExpression(ctx);
        if (ctx.bitwiseInclusiveOrExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.leftOperand =  (expr) property.get(ctx.bitwiseInclusiveOrExpression());
            treeNode.rightOperand = (expr) property.get(ctx.bitwiseExclusiveOrExpression());
            treeNode.operator = binaryOp.BITWISE_INCLUSIVE_OR;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.bitwiseExclusiveOrExpression()));
        }
    }

    @Override
    public void exitBitwiseExclusiveOrExpression(MxParser.BitwiseExclusiveOrExpressionContext ctx) {
        super.exitBitwiseExclusiveOrExpression(ctx);
        if (ctx.bitwiseExclusiveOrExpression() != null) {
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.operator = binaryOp.BITWISE_EXCLUSIVE_OR;
            treeNode.leftOperand = (expr) property.get(ctx.bitwiseExclusiveOrExpression());
            treeNode.rightOperand = (expr) property.get(ctx.bitwiseAndExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.bitwiseAndExpression()));
        }
    }

    @Override
    public void exitBitwiseAndExpression(MxParser.BitwiseAndExpressionContext ctx) {
        super.exitBitwiseAndExpression(ctx);
        if (ctx.bitwiseAndExpression() != null) {
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.operator = binaryOp.BITWISE_AND;
            treeNode.leftOperand = (expr) property.get(ctx.bitwiseAndExpression());
            treeNode.rightOperand = (expr) property.get(ctx.equalityExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.equalityExpression()));
        }
    }
    @Override
    public void exitEqualityExpression(MxParser.EqualityExpressionContext ctx) {
        super.exitEqualityExpression(ctx);
        if (ctx.equalityExpression() != null) {
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            if (ctx.Equal() != null) {
                treeNode.operator = binaryOp.EQUAL;
            }
            else {
                treeNode.operator = binaryOp.NOT_EQUAL;
            }
            treeNode.leftOperand = (expr) property.get(ctx.equalityExpression());
            treeNode.rightOperand =  (expr) property.get(ctx.relationExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.relationExpression()));
        }
    }
    @Override
    public void exitRelationExpression(MxParser.RelationExpressionContext ctx) {
        super.exitRelationExpression(ctx);
        if (ctx.relationExpression() != null) {
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            if (ctx.Less() != null){
                treeNode.operator = binaryOp.LESS;
            }
            else if (ctx.LessEqual() != null){
                treeNode.operator = binaryOp.LEQ;
            }
            else if (ctx.Greater() != null){
                treeNode.operator = binaryOp.GREATER;
            }
            else if (ctx.GreaterEqual() != null){
                treeNode.operator = binaryOp.GEQ;
            }
            treeNode.rightOperand = (expr) property.get(ctx.shiftExpression());
            treeNode.leftOperand = (expr) property.get(ctx.relationExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx,property.get(ctx.shiftExpression()));
        }
    }

    @Override
    public void exitShiftExpression(MxParser.ShiftExpressionContext ctx) {
        super.exitShiftExpression(ctx);
        if (ctx.shiftExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.leftOperand = (expr) property.get(ctx.shiftExpression());
            treeNode.rightOperand = (expr) property.get(ctx.additiveExpression());
            if (ctx.LeftShift() != null){
                treeNode.operator = binaryOp.LEFT_SHIFT;
            }
            else {
                treeNode.operator = binaryOp.RIGHT_SHIFT;
            }
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        }
        else {
            property.put(ctx, property.get(ctx.additiveExpression()));
        }
    }

    @Override
    public void exitAdditiveExpression(MxParser.AdditiveExpressionContext ctx) {
        super.exitAdditiveExpression(ctx);
        if (ctx.additiveExpression() != null){
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.leftOperand = (expr) property.get(ctx.additiveExpression());
            treeNode.rightOperand = (expr) property.get(ctx.multiplicativeExpression());
            if (ctx.Plus() != null){
                treeNode.operator = binaryOp.ADD;
            }
            else{
                treeNode.operator = binaryOp.SUB;
            }
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        }
        else {
            property.put(ctx, property.get(ctx.multiplicativeExpression()));
        }
    }

    @Override
    public void exitMultiplicativeExpression(MxParser.MultiplicativeExpressionContext ctx) {
        super.exitMultiplicativeExpression(ctx);
        if (ctx.multiplicativeExpression() != null) {
            binaryExpr treeNode = new binaryExpr();
            property.put(ctx, treeNode);
            treeNode.leftOperand = (expr) property.get(ctx.multiplicativeExpression());
            treeNode.rightOperand = (expr) property.get(ctx.unaryExpression());
            if (ctx.Star() != null) {
                treeNode.operator = binaryOp.MUL;
            }
            else if (ctx.Div() != null){
                treeNode.operator = binaryOp.DIV;
            }
            else if (ctx.Mod() != null){
                treeNode.operator = binaryOp.MOD;
            }
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.unaryExpression()));
        }
    }

    @Override
    public void exitUnaryExpression(MxParser.UnaryExpressionContext ctx) {
        super.exitUnaryExpression(ctx);
        if (ctx.PlusPlus() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            treeNode.operator = unaryOp.INCREMENT;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        }
        else if (ctx.MinusMinus() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            treeNode.operator = unaryOp.DECREMENT;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Tilde() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operator = unaryOp.BITWISE_NOT;
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Not() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            treeNode.operator = unaryOp.LOGIC_NOT;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Plus() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            treeNode.operator = unaryOp.POS;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Minus() != null){
            unaryExpr treeNode = new unaryExpr();
            property.put(ctx, treeNode);
            treeNode.operand = (expr) property.get(ctx.suffixExpression());
            treeNode.operator = unaryOp.NEG;
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else {
            property.put(ctx, property.get(ctx.suffixExpression()));
        }
    }

    @Override
    public void exitSuffixExpression(MxParser.SuffixExpressionContext ctx) {
        super.exitSuffixExpression(ctx);
        if (ctx.suffixExpression() != null){
            if (ctx.Dot() != null){
                if(ctx.LeftParen() != null){
                 /*   fieldfuncAccessExpr treeNode = new fieldfuncAccessExpr();
                    property.put(ctx,treeNode);
                    treeNode.funcName = ctx.Identifier().getText();
                    treeNode.obj = (expr)property.get(ctx.suffixExpression());
                    for (MxParser.ExpressionContext p : ctx.expression()){
                        treeNode.parameters.add((expr)property.get(p));
                    }
                    property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());*/
                }
                else {
                    fieldmemAccessExpr treeNode = new fieldmemAccessExpr();
                    property.put(ctx, treeNode);
                    treeNode.name = ctx.Identifier().getText();
                    treeNode.obj = (expr) property.get(ctx.suffixExpression());
                    property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
                }
            }
            else if (ctx.MinusMinus() != null){
                selfDec treeNode = new selfDec();
                property.put(ctx, treeNode);
                treeNode.operand = (expr) property.get(ctx.suffixExpression());
                property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
            else if (ctx.PlusPlus() != null){
                selfInc treeNode = new selfInc();
                property.put(ctx, treeNode);
                treeNode.operand = (expr) property.get(ctx.suffixExpression());
                property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
            else if (ctx.LeftBracket() != null){
                indexAccessExpr treeNode = new indexAccessExpr();
                property.put(ctx, treeNode);
                treeNode.array = (expr) property.get(ctx.suffixExpression());
                treeNode.index = (expr) property.get(ctx.expression(0));
                property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
            else if (ctx.LeftParen() != null){
                if(ctx.suffixExpression().Dot() != null) {
                    fieldfuncAccessExpr treeNode = new fieldfuncAccessExpr();
                    treeNode.name = ctx.suffixExpression().Identifier().getText();
                    property.put(ctx, treeNode);
                    treeNode.obj = (expr)property.get(ctx.suffixExpression().suffixExpression());
                    for (MxParser.ExpressionContext p : ctx.expression()){
                        treeNode.parameters.add((expr)property.get(p));
                    }
                    property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
                }
                else {
                    funcCall treeNode = new funcCall();
                    property.put(ctx, treeNode);
                    treeNode.obj = (expr) property.get(ctx.suffixExpression());
                    for (MxParser.ExpressionContext p : ctx.expression()) {
                        treeNode.parameters.add((expr) property.get(p));
                    }
                    property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
                }
            }
        }
        else{
            property.put(ctx, property.get(ctx.primaryExpression()));
        }
    }

    @Override
    public void exitInit(MxParser.InitContext ctx) {
        super.exitInit(ctx);
        property.put(ctx, property.get(ctx.expression()));
    }

    @Override
    public void exitCondition(MxParser.ConditionContext ctx) {
        super.exitCondition(ctx);
        property.put(ctx, property.get(ctx.expression()));
    }

    @Override
    public void exitStep(MxParser.StepContext ctx) {
        super.exitStep(ctx);
        property.put(ctx, property.get(ctx.expression()));
    }

    @Override
    public void exitPrimaryExpression(MxParser.PrimaryExpressionContext ctx) {
        super.exitPrimaryExpression(ctx);
        if (ctx.Identifier() != null){
            identifier treeNode = new identifier();
            property.put(ctx, treeNode);
            treeNode.name = ctx.Identifier().getText();
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.constant() != null){
            property.put(ctx, property.get(ctx.constant()));
        }
        else if (ctx.LeftParen() != null){
            property.put(ctx, property.get(ctx.expression()));
        }
        else if (ctx.New() != null){
            newExpr treeNode = new newExpr();
            property.put(ctx, treeNode);
            treeNode.newName = (typ) property.get(ctx.typeSpecifier());
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
    }

    @Override
    public void exitConstant(MxParser.ConstantContext ctx) {
        super.exitConstant(ctx);
        if (ctx.True() != null){
            boolConstant treeNode = new boolConstant();
            treeNode.value = true;
            property.put(ctx, treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.False() != null){
            boolConstant treeNode = new boolConstant();
            treeNode.value = false;
            property.put(ctx, treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());        }
        else if (ctx.Null() != null){
            Null treeNode = new Null();
            property.put(ctx, treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.StringConstant() != null){
            stringConstant treeNode = new stringConstant();
            treeNode.value = ctx.getText();
            property.put(ctx, treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.IntegerConstant() != null){
            intConstant treeNode = new intConstant();
            treeNode.value = new BigInteger(ctx.getText());
            property.put(ctx, treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }

    }

    @Override
    public void exitTypeSpecifier(MxParser.TypeSpecifierContext ctx) {
        super.exitTypeSpecifier(ctx);
        if(ctx.Bool() != null){
            boolType treeNode = new boolType();
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Int() != null){
            intType treeNode = new intType();
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.String() != null){
            stringType treeNode = new stringType();
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Void() != null){
            voidType treeNode = new voidType();
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.Identifier() != null){
            classType treeNode = new classType();
            treeNode.name = ctx.getText();
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        else if (ctx.typeSpecifier() != null){
            arrayType treeNode = new arrayType();
            treeNode.baseType = (typ) property.get(ctx.typeSpecifier());
            if(ctx.expression() != null) {
                treeNode.index = (expr) property.get(ctx.expression());
            }
            property.put(ctx,treeNode);
            property.get(ctx).loc = new location(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        }
    }
}
