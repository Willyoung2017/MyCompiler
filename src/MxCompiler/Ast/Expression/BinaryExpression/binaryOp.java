package MxCompiler.Ast.Expression.BinaryExpression;

public enum binaryOp {
    ASSIGN,
    LOGICAL_OR, LOGICAL_AND, BITWISE_INCLUSIVE_OR, BITWISE_EXCLUSIVE_OR, BITWISE_AND, // || && | ^ &
    EQUAL, NOT_EQUAL, LESS, LEQ, GREATER, GEQ, LEFT_SHIFT, RIGHT_SHIFT,
    ADD, SUB, MUL, DIV, MOD
}
