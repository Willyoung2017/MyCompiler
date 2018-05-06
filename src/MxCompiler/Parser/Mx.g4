grammar Mx;

program
    :   externalDeclaration+
    ;

externalDeclaration
    :   functionDeclaration
    |   classDeclaration
    |   globalVariableDeclaration
    ;

functionDeclaration
    :   typeSpecifier Identifier '(' (variableDeclaration (',' variableDeclaration)*)? ')' compoundStatement
    ;

classDeclaration
    :   Class Identifier '{' memberDeclaration* '}'
    ;

globalVariableDeclaration
    :   typeSpecifier Identifier ('=' expression)? ';'
    ;

variableDeclaration
    :   typeSpecifier Identifier ('=' expression)?
    ;

constructFunctionDeclaration
    :   Identifier '(' (variableDeclaration (',' variableDeclaration)*)? ')' compoundStatement
    ;

statement
    :   compoundStatement
    |   expressionStatement
    |   selectionStatement
    |   iterationStatement
    |   jumpStatement
    |   variableDeclarationStatement
    ;

compoundStatement
    :   '{' statement* '}'
    ;

expressionStatement
    : expression? ';'
    ;

selectionStatement
    : If '(' expression ')' statement (Else statement)?
    ;

iterationStatement
    :   While '(' expression ')' statement
    |   For '(' init? ';' condition? ';' step? ')' statement
    ;

jumpStatement
    :   Break ';'
    |   Continue ';'
    |   Return expression? ';'
    ;

variableDeclarationStatement
    :   typeSpecifier Identifier ('=' expression)? ';'
    ;

memberDeclaration
    :   variableDeclaration ';'
    |   functionDeclaration
    |   constructFunctionDeclaration
    ;

expression
    :   assignmentExpression
    ;

assignmentExpression
    :   logicalOrExpression
    |   unaryExpression '=' assignmentExpression
    ;

logicalOrExpression
    :   logicalAndExpression
    |   logicalOrExpression '||' logicalAndExpression
    ;

logicalAndExpression
    :   bitwiseInclusiveOrExpression
    |   logicalAndExpression '&&' bitwiseInclusiveOrExpression
    ;

bitwiseInclusiveOrExpression
    :   bitwiseExclusiveOrExpression
    |   bitwiseInclusiveOrExpression '|' bitwiseExclusiveOrExpression
    ;

bitwiseExclusiveOrExpression
    :   bitwiseAndExpression
    |   bitwiseExclusiveOrExpression '^' bitwiseAndExpression
    ;

bitwiseAndExpression
    :   equalityExpression
    |   bitwiseAndExpression '&' equalityExpression
    ;

equalityExpression
    :   relationExpression
    |   equalityExpression Equal relationExpression
    |   equalityExpression NotEqual relationExpression
    ;

relationExpression
    :   shiftExpression
    |   relationExpression Less shiftExpression
    |   relationExpression LessEqual shiftExpression
    |   relationExpression Greater shiftExpression
    |   relationExpression GreaterEqual shiftExpression
    ;

shiftExpression
    :   additiveExpression
    |   shiftExpression LeftShift additiveExpression
    |   shiftExpression RightShift additiveExpression
    ;

additiveExpression
    :   multiplicativeExpression
    |   additiveExpression Plus multiplicativeExpression
    |   additiveExpression Minus multiplicativeExpression
    ;

multiplicativeExpression
    :   unaryExpression
    |   multiplicativeExpression Star unaryExpression
    |   multiplicativeExpression Div unaryExpression
    |   multiplicativeExpression Mod unaryExpression
    ;

unaryExpression
    :   suffixExpression
    |   PlusPlus suffixExpression
    |   MinusMinus suffixExpression
    |   Tilde suffixExpression
    |   Not suffixExpression
    |   Plus suffixExpression
    |   Minus suffixExpression
    |   Tilde unaryExpression
    |   Minus unaryExpression
    ;

suffixExpression
    :   primaryExpression
    |   suffixExpression Dot Identifier  // classMem
    |   suffixExpression Dot Identifier LeftParen (expression (',' expression)*)? RightParen // classFunc
    |   suffixExpression LeftBracket expression RightBracket  // array
    |   suffixExpression LeftParen (expression (',' expression)*)? RightParen  // functioncall
    |   suffixExpression PlusPlus
    |   suffixExpression MinusMinus
    ;


init
    :   expression
    ;

condition
    :   expression
    ;

step
    :   expression
    ;

primaryExpression
    :   Identifier
    |   constant
    |   LeftParen expression RightParen
    |   New typeSpecifier ('('')')?
    ;

constant
    :   True
    |   False
    |   IntegerConstant
    |   StringConstant
    |   Null
    ;

typeSpecifier
    :   Bool
    |   Int
    |   String
    |   Void
    |   Identifier
    |   typeSpecifier LeftBracket expression? RightBracket
    ;

// keywards
Bool : 'bool';
Int : 'int';
String : 'string';
Null : 'null';
Void : 'void';
True : 'true';
False : 'false';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
New : 'new';
Class : 'class';

Identifier
    :   Nondigit1
        (   Nondigit
        |   Digit
        )*
    ;

//fvck : StringConstant;
fragment
Nondigit1
    :   [a-zA-Z]
    ;

fragment
Nondigit
    :   [a-zA-Z_]
    ;

fragment
Digit
    :   [0-9]
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;

IntegerConstant
    :   NonzeroDigit Digit*
    |   '0'
    ;

StringConstant
    :   '"' Char* '"'
    ;

fragment
Char
    :   PrintableChar
    |   EscapeChar
    ;

fragment
PrintableChar
    :   ~["\\\r\n]
    ;

fragment
EscapeChar
    :   '\\' ['"?abfnrtv\\]
    ;

// Operator

// Arithmetic Operator
Plus : '+';
Minus : '-';
Star : '*';
Div : '/';
Mod : '%';

//  Relation Operator
Less : '<';
Greater : '>';
Equal : '==';
NotEqual : '!=';
GreaterEqual : '>=';
LessEqual : '<=';

// Logical Operator
AndAnd : '&&';
OrOr : '||';
Not : '!';

// Bitwise Operator
LeftShift :  '<<';
RightShift : '>>';
Tilde : '~';
Or : '|';
Caret : '^';
And : '&';

// Assignment Operator
Assign : '=';

// Suffix Operator
PlusPlus : '++';
MinusMinus : '--';

//  Component Operator
Dot : '.';

//  Subscript Operator
LeftBracket : '[';
RightBracket : ']';

//  Parentthesis
LeftParen : '(';
RightParen : ')';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

WhiteSpace
    :   [ \t]+
        -> skip
    ;

NewLine
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;