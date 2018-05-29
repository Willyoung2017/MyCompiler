// Generated from Mx.g4 by ANTLR 4.5
package MxCompiler.Parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, Bool=3, Int=4, String=5, Null=6, Void=7, True=8, False=9, 
		If=10, Else=11, For=12, While=13, Break=14, Continue=15, Return=16, New=17, 
		Class=18, Identifier=19, IntegerConstant=20, StringConstant=21, Plus=22, 
		Minus=23, Star=24, Div=25, Mod=26, Less=27, Greater=28, Equal=29, NotEqual=30, 
		GreaterEqual=31, LessEqual=32, AndAnd=33, OrOr=34, Not=35, LeftShift=36, 
		RightShift=37, Tilde=38, Or=39, Caret=40, And=41, Assign=42, PlusPlus=43, 
		MinusMinus=44, Dot=45, LeftBracket=46, RightBracket=47, LeftParen=48, 
		RightParen=49, Question=50, Colon=51, Semi=52, Comma=53, WhiteSpace=54, 
		NewLine=55, LineComment=56, Comments=57;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "Bool", "Int", "String", "Null", "Void", "True", "False", 
		"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
		"Identifier", "Nondigit1", "Nondigit", "Digit", "NonzeroDigit", "IntegerConstant", 
		"StringConstant", "Char", "PrintableChar", "EscapeChar", "Plus", "Minus", 
		"Star", "Div", "Mod", "Less", "Greater", "Equal", "NotEqual", "GreaterEqual", 
		"LessEqual", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", "Tilde", 
		"Or", "Caret", "And", "Assign", "PlusPlus", "MinusMinus", "Dot", "LeftBracket", 
		"RightBracket", "LeftParen", "RightParen", "Question", "Colon", "Semi", 
		"Comma", "WhiteSpace", "NewLine", "LineComment", "Comments"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'{'", "'}'", "'bool'", "'int'", "'string'", "'null'", "'void'", 
		"'true'", "'false'", "'if'", "'else'", "'for'", "'while'", "'break'", 
		"'continue'", "'return'", "'new'", "'class'", null, null, null, "'+'", 
		"'-'", "'*'", "'/'", "'%'", "'<'", "'>'", "'=='", "'!='", "'>='", "'<='", 
		"'&&'", "'||'", "'!'", "'<<'", "'>>'", "'~'", "'|'", "'^'", "'&'", "'='", 
		"'++'", "'--'", "'.'", "'['", "']'", "'('", "')'", "'?'", "':'", "';'", 
		"','"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "Bool", "Int", "String", "Null", "Void", "True", "False", 
		"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
		"Identifier", "IntegerConstant", "StringConstant", "Plus", "Minus", "Star", 
		"Div", "Mod", "Less", "Greater", "Equal", "NotEqual", "GreaterEqual", 
		"LessEqual", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", "Tilde", 
		"Or", "Caret", "And", "Assign", "PlusPlus", "MinusMinus", "Dot", "LeftBracket", 
		"RightBracket", "LeftParen", "RightParen", "Question", "Colon", "Semi", 
		"Comma", "WhiteSpace", "NewLine", "LineComment", "Comments"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2;\u017d\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\7\24\u00e2\n\24\f\24\16\24\u00e5\13\24\3\25"+
		"\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\7\31\u00f1\n\31\f\31\16"+
		"\31\u00f4\13\31\3\31\5\31\u00f7\n\31\3\32\3\32\7\32\u00fb\n\32\f\32\16"+
		"\32\u00fe\13\32\3\32\3\32\3\33\3\33\5\33\u0104\n\33\3\34\3\34\3\35\3\35"+
		"\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3%\3&"+
		"\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3,\3,\3,\3-\3-\3-"+
		"\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\63\3\64\3\64\3"+
		"\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3="+
		"\3>\6>\u0156\n>\r>\16>\u0157\3>\3>\3?\3?\5?\u015e\n?\3?\5?\u0161\n?\3"+
		"?\3?\3@\3@\3@\3@\7@\u0169\n@\f@\16@\u016c\13@\3@\3@\3A\3A\3A\3A\7A\u0174"+
		"\nA\fA\16A\u0177\13A\3A\3A\3A\3A\3A\3\u0175\2B\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\2+\2-\2/\2\61\26\63\27\65\2\67\29\2;\30=\31?\32A\33C\34E\35G\36I\37"+
		"K M!O\"Q#S$U%W&Y\'[(])_*a+c,e-g.i/k\60m\61o\62q\63s\64u\65w\66y\67{8}"+
		"9\177:\u0081;\3\2\13\4\2C\\c|\5\2C\\aac|\3\2\62;\3\2\63;\6\2\f\f\17\17"+
		"$$^^\f\2$$))AA^^cdhhppttvvxx\4\2\13\13\"\"\4\2\f\f\17\17\4\2UU^^\u0180"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2"+
		"W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3"+
		"\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2"+
		"\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2"+
		"}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\3\u0083\3\2\2\2\5\u0085\3\2\2"+
		"\2\7\u0087\3\2\2\2\t\u008c\3\2\2\2\13\u0090\3\2\2\2\r\u0097\3\2\2\2\17"+
		"\u009c\3\2\2\2\21\u00a1\3\2\2\2\23\u00a6\3\2\2\2\25\u00ac\3\2\2\2\27\u00af"+
		"\3\2\2\2\31\u00b4\3\2\2\2\33\u00b8\3\2\2\2\35\u00be\3\2\2\2\37\u00c4\3"+
		"\2\2\2!\u00cd\3\2\2\2#\u00d4\3\2\2\2%\u00d8\3\2\2\2\'\u00de\3\2\2\2)\u00e6"+
		"\3\2\2\2+\u00e8\3\2\2\2-\u00ea\3\2\2\2/\u00ec\3\2\2\2\61\u00f6\3\2\2\2"+
		"\63\u00f8\3\2\2\2\65\u0103\3\2\2\2\67\u0105\3\2\2\29\u0107\3\2\2\2;\u010a"+
		"\3\2\2\2=\u010c\3\2\2\2?\u010e\3\2\2\2A\u0110\3\2\2\2C\u0112\3\2\2\2E"+
		"\u0114\3\2\2\2G\u0116\3\2\2\2I\u0118\3\2\2\2K\u011b\3\2\2\2M\u011e\3\2"+
		"\2\2O\u0121\3\2\2\2Q\u0124\3\2\2\2S\u0127\3\2\2\2U\u012a\3\2\2\2W\u012c"+
		"\3\2\2\2Y\u012f\3\2\2\2[\u0132\3\2\2\2]\u0134\3\2\2\2_\u0136\3\2\2\2a"+
		"\u0138\3\2\2\2c\u013a\3\2\2\2e\u013c\3\2\2\2g\u013f\3\2\2\2i\u0142\3\2"+
		"\2\2k\u0144\3\2\2\2m\u0146\3\2\2\2o\u0148\3\2\2\2q\u014a\3\2\2\2s\u014c"+
		"\3\2\2\2u\u014e\3\2\2\2w\u0150\3\2\2\2y\u0152\3\2\2\2{\u0155\3\2\2\2}"+
		"\u0160\3\2\2\2\177\u0164\3\2\2\2\u0081\u016f\3\2\2\2\u0083\u0084\7}\2"+
		"\2\u0084\4\3\2\2\2\u0085\u0086\7\177\2\2\u0086\6\3\2\2\2\u0087\u0088\7"+
		"d\2\2\u0088\u0089\7q\2\2\u0089\u008a\7q\2\2\u008a\u008b\7n\2\2\u008b\b"+
		"\3\2\2\2\u008c\u008d\7k\2\2\u008d\u008e\7p\2\2\u008e\u008f\7v\2\2\u008f"+
		"\n\3\2\2\2\u0090\u0091\7u\2\2\u0091\u0092\7v\2\2\u0092\u0093\7t\2\2\u0093"+
		"\u0094\7k\2\2\u0094\u0095\7p\2\2\u0095\u0096\7i\2\2\u0096\f\3\2\2\2\u0097"+
		"\u0098\7p\2\2\u0098\u0099\7w\2\2\u0099\u009a\7n\2\2\u009a\u009b\7n\2\2"+
		"\u009b\16\3\2\2\2\u009c\u009d\7x\2\2\u009d\u009e\7q\2\2\u009e\u009f\7"+
		"k\2\2\u009f\u00a0\7f\2\2\u00a0\20\3\2\2\2\u00a1\u00a2\7v\2\2\u00a2\u00a3"+
		"\7t\2\2\u00a3\u00a4\7w\2\2\u00a4\u00a5\7g\2\2\u00a5\22\3\2\2\2\u00a6\u00a7"+
		"\7h\2\2\u00a7\u00a8\7c\2\2\u00a8\u00a9\7n\2\2\u00a9\u00aa\7u\2\2\u00aa"+
		"\u00ab\7g\2\2\u00ab\24\3\2\2\2\u00ac\u00ad\7k\2\2\u00ad\u00ae\7h\2\2\u00ae"+
		"\26\3\2\2\2\u00af\u00b0\7g\2\2\u00b0\u00b1\7n\2\2\u00b1\u00b2\7u\2\2\u00b2"+
		"\u00b3\7g\2\2\u00b3\30\3\2\2\2\u00b4\u00b5\7h\2\2\u00b5\u00b6\7q\2\2\u00b6"+
		"\u00b7\7t\2\2\u00b7\32\3\2\2\2\u00b8\u00b9\7y\2\2\u00b9\u00ba\7j\2\2\u00ba"+
		"\u00bb\7k\2\2\u00bb\u00bc\7n\2\2\u00bc\u00bd\7g\2\2\u00bd\34\3\2\2\2\u00be"+
		"\u00bf\7d\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1\7g\2\2\u00c1\u00c2\7c\2\2"+
		"\u00c2\u00c3\7m\2\2\u00c3\36\3\2\2\2\u00c4\u00c5\7e\2\2\u00c5\u00c6\7"+
		"q\2\2\u00c6\u00c7\7p\2\2\u00c7\u00c8\7v\2\2\u00c8\u00c9\7k\2\2\u00c9\u00ca"+
		"\7p\2\2\u00ca\u00cb\7w\2\2\u00cb\u00cc\7g\2\2\u00cc \3\2\2\2\u00cd\u00ce"+
		"\7t\2\2\u00ce\u00cf\7g\2\2\u00cf\u00d0\7v\2\2\u00d0\u00d1\7w\2\2\u00d1"+
		"\u00d2\7t\2\2\u00d2\u00d3\7p\2\2\u00d3\"\3\2\2\2\u00d4\u00d5\7p\2\2\u00d5"+
		"\u00d6\7g\2\2\u00d6\u00d7\7y\2\2\u00d7$\3\2\2\2\u00d8\u00d9\7e\2\2\u00d9"+
		"\u00da\7n\2\2\u00da\u00db\7c\2\2\u00db\u00dc\7u\2\2\u00dc\u00dd\7u\2\2"+
		"\u00dd&\3\2\2\2\u00de\u00e3\5)\25\2\u00df\u00e2\5+\26\2\u00e0\u00e2\5"+
		"-\27\2\u00e1\u00df\3\2\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3"+
		"\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4(\3\2\2\2\u00e5\u00e3\3\2\2\2"+
		"\u00e6\u00e7\t\2\2\2\u00e7*\3\2\2\2\u00e8\u00e9\t\3\2\2\u00e9,\3\2\2\2"+
		"\u00ea\u00eb\t\4\2\2\u00eb.\3\2\2\2\u00ec\u00ed\t\5\2\2\u00ed\60\3\2\2"+
		"\2\u00ee\u00f2\5/\30\2\u00ef\u00f1\5-\27\2\u00f0\u00ef\3\2\2\2\u00f1\u00f4"+
		"\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f7\3\2\2\2\u00f4"+
		"\u00f2\3\2\2\2\u00f5\u00f7\7\62\2\2\u00f6\u00ee\3\2\2\2\u00f6\u00f5\3"+
		"\2\2\2\u00f7\62\3\2\2\2\u00f8\u00fc\7$\2\2\u00f9\u00fb\5\65\33\2\u00fa"+
		"\u00f9\3\2\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2"+
		"\2\2\u00fd\u00ff\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0100\7$\2\2\u0100"+
		"\64\3\2\2\2\u0101\u0104\5\67\34\2\u0102\u0104\59\35\2\u0103\u0101\3\2"+
		"\2\2\u0103\u0102\3\2\2\2\u0104\66\3\2\2\2\u0105\u0106\n\6\2\2\u01068\3"+
		"\2\2\2\u0107\u0108\7^\2\2\u0108\u0109\t\7\2\2\u0109:\3\2\2\2\u010a\u010b"+
		"\7-\2\2\u010b<\3\2\2\2\u010c\u010d\7/\2\2\u010d>\3\2\2\2\u010e\u010f\7"+
		",\2\2\u010f@\3\2\2\2\u0110\u0111\7\61\2\2\u0111B\3\2\2\2\u0112\u0113\7"+
		"\'\2\2\u0113D\3\2\2\2\u0114\u0115\7>\2\2\u0115F\3\2\2\2\u0116\u0117\7"+
		"@\2\2\u0117H\3\2\2\2\u0118\u0119\7?\2\2\u0119\u011a\7?\2\2\u011aJ\3\2"+
		"\2\2\u011b\u011c\7#\2\2\u011c\u011d\7?\2\2\u011dL\3\2\2\2\u011e\u011f"+
		"\7@\2\2\u011f\u0120\7?\2\2\u0120N\3\2\2\2\u0121\u0122\7>\2\2\u0122\u0123"+
		"\7?\2\2\u0123P\3\2\2\2\u0124\u0125\7(\2\2\u0125\u0126\7(\2\2\u0126R\3"+
		"\2\2\2\u0127\u0128\7~\2\2\u0128\u0129\7~\2\2\u0129T\3\2\2\2\u012a\u012b"+
		"\7#\2\2\u012bV\3\2\2\2\u012c\u012d\7>\2\2\u012d\u012e\7>\2\2\u012eX\3"+
		"\2\2\2\u012f\u0130\7@\2\2\u0130\u0131\7@\2\2\u0131Z\3\2\2\2\u0132\u0133"+
		"\7\u0080\2\2\u0133\\\3\2\2\2\u0134\u0135\7~\2\2\u0135^\3\2\2\2\u0136\u0137"+
		"\7`\2\2\u0137`\3\2\2\2\u0138\u0139\7(\2\2\u0139b\3\2\2\2\u013a\u013b\7"+
		"?\2\2\u013bd\3\2\2\2\u013c\u013d\7-\2\2\u013d\u013e\7-\2\2\u013ef\3\2"+
		"\2\2\u013f\u0140\7/\2\2\u0140\u0141\7/\2\2\u0141h\3\2\2\2\u0142\u0143"+
		"\7\60\2\2\u0143j\3\2\2\2\u0144\u0145\7]\2\2\u0145l\3\2\2\2\u0146\u0147"+
		"\7_\2\2\u0147n\3\2\2\2\u0148\u0149\7*\2\2\u0149p\3\2\2\2\u014a\u014b\7"+
		"+\2\2\u014br\3\2\2\2\u014c\u014d\7A\2\2\u014dt\3\2\2\2\u014e\u014f\7<"+
		"\2\2\u014fv\3\2\2\2\u0150\u0151\7=\2\2\u0151x\3\2\2\2\u0152\u0153\7.\2"+
		"\2\u0153z\3\2\2\2\u0154\u0156\t\b\2\2\u0155\u0154\3\2\2\2\u0156\u0157"+
		"\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\3\2\2\2\u0159"+
		"\u015a\b>\2\2\u015a|\3\2\2\2\u015b\u015d\7\17\2\2\u015c\u015e\7\f\2\2"+
		"\u015d\u015c\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u0161"+
		"\7\f\2\2\u0160\u015b\3\2\2\2\u0160\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162"+
		"\u0163\b?\2\2\u0163~\3\2\2\2\u0164\u0165\7\61\2\2\u0165\u0166\7\61\2\2"+
		"\u0166\u016a\3\2\2\2\u0167\u0169\n\t\2\2\u0168\u0167\3\2\2\2\u0169\u016c"+
		"\3\2\2\2\u016a\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016d\3\2\2\2\u016c"+
		"\u016a\3\2\2\2\u016d\u016e\b@\2\2\u016e\u0080\3\2\2\2\u016f\u0170\7\61"+
		"\2\2\u0170\u0171\7,\2\2\u0171\u0175\3\2\2\2\u0172\u0174\n\n\2\2\u0173"+
		"\u0172\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0176\3\2\2\2\u0175\u0173\3\2"+
		"\2\2\u0176\u0178\3\2\2\2\u0177\u0175\3\2\2\2\u0178\u0179\7,\2\2\u0179"+
		"\u017a\7\61\2\2\u017a\u017b\3\2\2\2\u017b\u017c\bA\2\2\u017c\u0082\3\2"+
		"\2\2\16\2\u00e1\u00e3\u00f2\u00f6\u00fc\u0103\u0157\u015d\u0160\u016a"+
		"\u0175\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}