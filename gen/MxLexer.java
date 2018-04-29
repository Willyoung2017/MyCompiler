// Generated from E:/compiler/MyCompiler/src\Mx.g4 by ANTLR 4.7
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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

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
		NewLine=55, LineComment=56;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "Bool", "Int", "String", "Null", "Void", "True", "False", 
		"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
		"Identifier", "Nondigit", "Digit", "NonzeroDigit", "IntegerConstant", 
		"StringConstant", "Char", "PrintableChar", "EscapeChar", "Plus", "Minus", 
		"Star", "Div", "Mod", "Less", "Greater", "Equal", "NotEqual", "GreaterEqual", 
		"LessEqual", "AndAnd", "OrOr", "Not", "LeftShift", "RightShift", "Tilde", 
		"Or", "Caret", "And", "Assign", "PlusPlus", "MinusMinus", "Dot", "LeftBracket", 
		"RightBracket", "LeftParen", "RightParen", "Question", "Colon", "Semi", 
		"Comma", "WhiteSpace", "NewLine", "LineComment"
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
		"Comma", "WhiteSpace", "NewLine", "LineComment"
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
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2:\u0169\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3"+
		"\24\3\24\3\24\7\24\u00de\n\24\f\24\16\24\u00e1\13\24\3\25\3\25\3\26\3"+
		"\26\3\27\3\27\3\30\3\30\7\30\u00eb\n\30\f\30\16\30\u00ee\13\30\3\30\5"+
		"\30\u00f1\n\30\3\31\3\31\7\31\u00f5\n\31\f\31\16\31\u00f8\13\31\3\31\3"+
		"\31\3\32\3\32\5\32\u00fe\n\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36"+
		"\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\3"+
		"\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3+\3+\3+\3,\3,\3,\3-\3-\3.\3.\3/\3"+
		"/\3\60\3\60\3\61\3\61\3\62\3\62\3\62\3\63\3\63\3\63\3\64\3\64\3\65\3\65"+
		"\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\6=\u0150\n=\r=\16"+
		"=\u0151\3=\3=\3>\3>\5>\u0158\n>\3>\5>\u015b\n>\3>\3>\3?\3?\3?\3?\7?\u0163"+
		"\n?\f?\16?\u0166\13?\3?\3?\2\2@\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\2+\2-\2/\26\61"+
		"\27\63\2\65\2\67\29\30;\31=\32?\33A\34C\35E\36G\37I K!M\"O#Q$S%U&W\'Y"+
		"([)]*_+a,c-e.g/i\60k\61m\62o\63q\64s\65u\66w\67y8{9}:\3\2\t\5\2C\\aac"+
		"|\3\2\62;\3\2\63;\6\2\f\f\17\17))^^\f\2$$))AA^^cdhhppttvvxx\3\2\13\13"+
		"\4\2\f\f\17\17\2\u016c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2"+
		"\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q"+
		"\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2"+
		"\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2"+
		"\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w"+
		"\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\3\177\3\2\2\2\5\u0081\3\2\2"+
		"\2\7\u0083\3\2\2\2\t\u0088\3\2\2\2\13\u008c\3\2\2\2\r\u0093\3\2\2\2\17"+
		"\u0098\3\2\2\2\21\u009d\3\2\2\2\23\u00a2\3\2\2\2\25\u00a8\3\2\2\2\27\u00ab"+
		"\3\2\2\2\31\u00b0\3\2\2\2\33\u00b4\3\2\2\2\35\u00ba\3\2\2\2\37\u00c0\3"+
		"\2\2\2!\u00c9\3\2\2\2#\u00d0\3\2\2\2%\u00d4\3\2\2\2\'\u00da\3\2\2\2)\u00e2"+
		"\3\2\2\2+\u00e4\3\2\2\2-\u00e6\3\2\2\2/\u00f0\3\2\2\2\61\u00f2\3\2\2\2"+
		"\63\u00fd\3\2\2\2\65\u00ff\3\2\2\2\67\u0101\3\2\2\29\u0104\3\2\2\2;\u0106"+
		"\3\2\2\2=\u0108\3\2\2\2?\u010a\3\2\2\2A\u010c\3\2\2\2C\u010e\3\2\2\2E"+
		"\u0110\3\2\2\2G\u0112\3\2\2\2I\u0115\3\2\2\2K\u0118\3\2\2\2M\u011b\3\2"+
		"\2\2O\u011e\3\2\2\2Q\u0121\3\2\2\2S\u0124\3\2\2\2U\u0126\3\2\2\2W\u0129"+
		"\3\2\2\2Y\u012c\3\2\2\2[\u012e\3\2\2\2]\u0130\3\2\2\2_\u0132\3\2\2\2a"+
		"\u0134\3\2\2\2c\u0136\3\2\2\2e\u0139\3\2\2\2g\u013c\3\2\2\2i\u013e\3\2"+
		"\2\2k\u0140\3\2\2\2m\u0142\3\2\2\2o\u0144\3\2\2\2q\u0146\3\2\2\2s\u0148"+
		"\3\2\2\2u\u014a\3\2\2\2w\u014c\3\2\2\2y\u014f\3\2\2\2{\u015a\3\2\2\2}"+
		"\u015e\3\2\2\2\177\u0080\7}\2\2\u0080\4\3\2\2\2\u0081\u0082\7\177\2\2"+
		"\u0082\6\3\2\2\2\u0083\u0084\7d\2\2\u0084\u0085\7q\2\2\u0085\u0086\7q"+
		"\2\2\u0086\u0087\7n\2\2\u0087\b\3\2\2\2\u0088\u0089\7k\2\2\u0089\u008a"+
		"\7p\2\2\u008a\u008b\7v\2\2\u008b\n\3\2\2\2\u008c\u008d\7u\2\2\u008d\u008e"+
		"\7v\2\2\u008e\u008f\7t\2\2\u008f\u0090\7k\2\2\u0090\u0091\7p\2\2\u0091"+
		"\u0092\7i\2\2\u0092\f\3\2\2\2\u0093\u0094\7p\2\2\u0094\u0095\7w\2\2\u0095"+
		"\u0096\7n\2\2\u0096\u0097\7n\2\2\u0097\16\3\2\2\2\u0098\u0099\7x\2\2\u0099"+
		"\u009a\7q\2\2\u009a\u009b\7k\2\2\u009b\u009c\7f\2\2\u009c\20\3\2\2\2\u009d"+
		"\u009e\7v\2\2\u009e\u009f\7t\2\2\u009f\u00a0\7w\2\2\u00a0\u00a1\7g\2\2"+
		"\u00a1\22\3\2\2\2\u00a2\u00a3\7h\2\2\u00a3\u00a4\7c\2\2\u00a4\u00a5\7"+
		"n\2\2\u00a5\u00a6\7u\2\2\u00a6\u00a7\7g\2\2\u00a7\24\3\2\2\2\u00a8\u00a9"+
		"\7k\2\2\u00a9\u00aa\7h\2\2\u00aa\26\3\2\2\2\u00ab\u00ac\7g\2\2\u00ac\u00ad"+
		"\7n\2\2\u00ad\u00ae\7u\2\2\u00ae\u00af\7g\2\2\u00af\30\3\2\2\2\u00b0\u00b1"+
		"\7h\2\2\u00b1\u00b2\7q\2\2\u00b2\u00b3\7t\2\2\u00b3\32\3\2\2\2\u00b4\u00b5"+
		"\7y\2\2\u00b5\u00b6\7j\2\2\u00b6\u00b7\7k\2\2\u00b7\u00b8\7n\2\2\u00b8"+
		"\u00b9\7g\2\2\u00b9\34\3\2\2\2\u00ba\u00bb\7d\2\2\u00bb\u00bc\7t\2\2\u00bc"+
		"\u00bd\7g\2\2\u00bd\u00be\7c\2\2\u00be\u00bf\7m\2\2\u00bf\36\3\2\2\2\u00c0"+
		"\u00c1\7e\2\2\u00c1\u00c2\7q\2\2\u00c2\u00c3\7p\2\2\u00c3\u00c4\7v\2\2"+
		"\u00c4\u00c5\7k\2\2\u00c5\u00c6\7p\2\2\u00c6\u00c7\7w\2\2\u00c7\u00c8"+
		"\7g\2\2\u00c8 \3\2\2\2\u00c9\u00ca\7t\2\2\u00ca\u00cb\7g\2\2\u00cb\u00cc"+
		"\7v\2\2\u00cc\u00cd\7w\2\2\u00cd\u00ce\7t\2\2\u00ce\u00cf\7p\2\2\u00cf"+
		"\"\3\2\2\2\u00d0\u00d1\7p\2\2\u00d1\u00d2\7g\2\2\u00d2\u00d3\7y\2\2\u00d3"+
		"$\3\2\2\2\u00d4\u00d5\7e\2\2\u00d5\u00d6\7n\2\2\u00d6\u00d7\7c\2\2\u00d7"+
		"\u00d8\7u\2\2\u00d8\u00d9\7u\2\2\u00d9&\3\2\2\2\u00da\u00df\5)\25\2\u00db"+
		"\u00de\5)\25\2\u00dc\u00de\5+\26\2\u00dd\u00db\3\2\2\2\u00dd\u00dc\3\2"+
		"\2\2\u00de\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		"(\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2\u00e3\t\2\2\2\u00e3*\3\2\2\2\u00e4"+
		"\u00e5\t\3\2\2\u00e5,\3\2\2\2\u00e6\u00e7\t\4\2\2\u00e7.\3\2\2\2\u00e8"+
		"\u00ec\5-\27\2\u00e9\u00eb\5+\26\2\u00ea\u00e9\3\2\2\2\u00eb\u00ee\3\2"+
		"\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00f1\3\2\2\2\u00ee"+
		"\u00ec\3\2\2\2\u00ef\u00f1\7\62\2\2\u00f0\u00e8\3\2\2\2\u00f0\u00ef\3"+
		"\2\2\2\u00f1\60\3\2\2\2\u00f2\u00f6\7$\2\2\u00f3\u00f5\5\63\32\2\u00f4"+
		"\u00f3\3\2\2\2\u00f5\u00f8\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f7\3\2"+
		"\2\2\u00f7\u00f9\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\u00fa\7$\2\2\u00fa"+
		"\62\3\2\2\2\u00fb\u00fe\5\65\33\2\u00fc\u00fe\5\67\34\2\u00fd\u00fb\3"+
		"\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\64\3\2\2\2\u00ff\u0100\n\5\2\2\u0100"+
		"\66\3\2\2\2\u0101\u0102\7^\2\2\u0102\u0103\t\6\2\2\u01038\3\2\2\2\u0104"+
		"\u0105\7-\2\2\u0105:\3\2\2\2\u0106\u0107\7/\2\2\u0107<\3\2\2\2\u0108\u0109"+
		"\7,\2\2\u0109>\3\2\2\2\u010a\u010b\7\61\2\2\u010b@\3\2\2\2\u010c\u010d"+
		"\7\'\2\2\u010dB\3\2\2\2\u010e\u010f\7>\2\2\u010fD\3\2\2\2\u0110\u0111"+
		"\7@\2\2\u0111F\3\2\2\2\u0112\u0113\7?\2\2\u0113\u0114\7?\2\2\u0114H\3"+
		"\2\2\2\u0115\u0116\7#\2\2\u0116\u0117\7?\2\2\u0117J\3\2\2\2\u0118\u0119"+
		"\7@\2\2\u0119\u011a\7?\2\2\u011aL\3\2\2\2\u011b\u011c\7>\2\2\u011c\u011d"+
		"\7?\2\2\u011dN\3\2\2\2\u011e\u011f\7(\2\2\u011f\u0120\7(\2\2\u0120P\3"+
		"\2\2\2\u0121\u0122\7~\2\2\u0122\u0123\7~\2\2\u0123R\3\2\2\2\u0124\u0125"+
		"\7#\2\2\u0125T\3\2\2\2\u0126\u0127\7>\2\2\u0127\u0128\7>\2\2\u0128V\3"+
		"\2\2\2\u0129\u012a\7@\2\2\u012a\u012b\7@\2\2\u012bX\3\2\2\2\u012c\u012d"+
		"\7\u0080\2\2\u012dZ\3\2\2\2\u012e\u012f\7~\2\2\u012f\\\3\2\2\2\u0130\u0131"+
		"\7`\2\2\u0131^\3\2\2\2\u0132\u0133\7(\2\2\u0133`\3\2\2\2\u0134\u0135\7"+
		"?\2\2\u0135b\3\2\2\2\u0136\u0137\7-\2\2\u0137\u0138\7-\2\2\u0138d\3\2"+
		"\2\2\u0139\u013a\7/\2\2\u013a\u013b\7/\2\2\u013bf\3\2\2\2\u013c\u013d"+
		"\7\60\2\2\u013dh\3\2\2\2\u013e\u013f\7]\2\2\u013fj\3\2\2\2\u0140\u0141"+
		"\7_\2\2\u0141l\3\2\2\2\u0142\u0143\7*\2\2\u0143n\3\2\2\2\u0144\u0145\7"+
		"+\2\2\u0145p\3\2\2\2\u0146\u0147\7A\2\2\u0147r\3\2\2\2\u0148\u0149\7<"+
		"\2\2\u0149t\3\2\2\2\u014a\u014b\7=\2\2\u014bv\3\2\2\2\u014c\u014d\7.\2"+
		"\2\u014dx\3\2\2\2\u014e\u0150\t\7\2\2\u014f\u014e\3\2\2\2\u0150\u0151"+
		"\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0154\b=\2\2\u0154z\3\2\2\2\u0155\u0157\7\17\2\2\u0156\u0158\7\f\2\2"+
		"\u0157\u0156\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015b\3\2\2\2\u0159\u015b"+
		"\7\f\2\2\u015a\u0155\3\2\2\2\u015a\u0159\3\2\2\2\u015b\u015c\3\2\2\2\u015c"+
		"\u015d\b>\2\2\u015d|\3\2\2\2\u015e\u015f\7\61\2\2\u015f\u0160\7\61\2\2"+
		"\u0160\u0164\3\2\2\2\u0161\u0163\n\b\2\2\u0162\u0161\3\2\2\2\u0163\u0166"+
		"\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166"+
		"\u0164\3\2\2\2\u0167\u0168\b?\2\2\u0168~\3\2\2\2\r\2\u00dd\u00df\u00ec"+
		"\u00f0\u00f6\u00fd\u0151\u0157\u015a\u0164\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}