// Generated from E:/compiler/MyCompiler/src/Parser\Mx.g4 by ANTLR 4.7
package MxCompiler.Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#externalDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalDeclaration(MxParser.ExternalDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#globalVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalVariableDeclaration(MxParser.GlobalVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constructFunctionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructFunctionDeclaration(MxParser.ConstructFunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(MxParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(MxParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#selectionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionStatement(MxParser.SelectionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#iterationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationStatement(MxParser.IterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#jumpStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpStatement(MxParser.JumpStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationStatement(MxParser.VariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(MxParser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MxParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#assignmentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpression(MxParser.AssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(MxParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(MxParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#bitwiseInclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseInclusiveOrExpression(MxParser.BitwiseInclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#bitwiseExclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseExclusiveOrExpression(MxParser.BitwiseExclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#bitwiseAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseAndExpression(MxParser.BitwiseAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(MxParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationExpression(MxParser.RelationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpression(MxParser.ShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(MxParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(MxParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(MxParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#suffixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixExpression(MxParser.SuffixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit(MxParser.InitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(MxParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#step}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStep(MxParser.StepContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(MxParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(MxParser.TypeSpecifierContext ctx);
}