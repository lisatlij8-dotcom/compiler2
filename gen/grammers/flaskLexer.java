// Generated from grammers/flaskLexer.g4 by ANTLR 4.13.2
package grammers;

    import java.util.LinkedList;
    import java.util.Stack;
    import org.antlr.v4.runtime.CommonToken;
    import org.antlr.v4.runtime.Token;
    import org.antlr.v4.runtime.misc.Pair;
    import org.antlr.v4.runtime.CharStream;
    import org.antlr.v4.runtime.TokenSource;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class flaskLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INDENT=1, DEDENT=2, LPAREN=3, RPAREN=4, LBRACE=5, RBRACE=6, LBRACK=7, 
		RBRACK=8, NEWLINE=9, DEF=10, RETURN=11, IF=12, ELSE=13, ELIF=14, FOR=15, 
		IN=16, IMPORT=17, FROM=18, AS=19, TRUE=20, FALSE=21, NONE=22, AND=23, 
		OR=24, NOT=25, PLUS=26, MINUS=27, MULT=28, DIV=29, EQ=30, ASSIGN=31, NEQ=32, 
		LT=33, GT=34, LE=35, GE=36, COMMA=37, COLON=38, DOT=39, AT=40, NUMBER=41, 
		STRING=42, ID=43, WS=44, HASH_COMMENT=45;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "IND_WS", 
			"NEWLINE", "DEF", "RETURN", "IF", "ELSE", "ELIF", "FOR", "IN", "IMPORT", 
			"FROM", "AS", "TRUE", "FALSE", "NONE", "AND", "OR", "NOT", "PLUS", "MINUS", 
			"MULT", "DIV", "EQ", "ASSIGN", "NEQ", "LT", "GT", "LE", "GE", "COMMA", 
			"COLON", "DOT", "AT", "NUMBER", "STRING", "ID", "WS", "HASH_COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", null, "'def'", 
			"'return'", "'if'", "'else'", "'elif'", "'for'", "'in'", "'import'", 
			"'from'", "'as'", "'True'", "'False'", "'None'", "'and'", "'or'", "'not'", 
			"'+'", "'-'", "'*'", "'/'", "'=='", "'='", "'!='", "'<'", "'>'", "'<='", 
			"'>='", "','", "':'", "'.'", "'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INDENT", "DEDENT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", 
			"RBRACK", "NEWLINE", "DEF", "RETURN", "IF", "ELSE", "ELIF", "FOR", "IN", 
			"IMPORT", "FROM", "AS", "TRUE", "FALSE", "NONE", "AND", "OR", "NOT", 
			"PLUS", "MINUS", "MULT", "DIV", "EQ", "ASSIGN", "NEQ", "LT", "GT", "LE", 
			"GE", "COMMA", "COLON", "DOT", "AT", "NUMBER", "STRING", "ID", "WS", 
			"HASH_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	    private Stack<Integer> indentLevels = new Stack<>();
	    private LinkedList<Token> pendingTokens = new LinkedList<>();
	    private int nestingLevel = 0;

	    {
	        indentLevels.push(0);
	    }

	    @Override
	    public void reset() {
	        indentLevels.clear();
	        indentLevels.push(0);
	        pendingTokens.clear();
	        nestingLevel = 0;
	        super.reset();
	    }

	    @Override
	    public Token nextToken() {
	        if (!pendingTokens.isEmpty())
	            return pendingTokens.poll();

	        Token t = super.nextToken();

	        if (t.getType() == EOF) {
	            if (!pendingTokens.isEmpty()) {

	            } else {

	                pendingTokens.add(createToken(NEWLINE, "\n"));
	            }
	            while (indentLevels.size() > 1) {
	                indentLevels.pop();
	                pendingTokens.add(createToken(DEDENT, ""));
	            }

	            pendingTokens.add(t);
	            return pendingTokens.poll();
	        }

	        return t;
	    }


	    private CommonToken createToken(int type, String text) {
	        int stop = getCharIndex() - 1;
	        int start = text.isEmpty() ? stop : stop - text.length() + 1;
	        Pair<TokenSource, CharStream> source = new Pair<>(this, getInputStream());
	        CommonToken t = new CommonToken(source, type, Token.DEFAULT_CHANNEL, start, stop);
	        t.setText(text);
	        return t;
	    }


	public flaskLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "flaskLexer.g4"; }

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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0:
			LPAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 1:
			RPAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 2:
			LBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 3:
			RBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 4:
			LBRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 5:
			RBRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 7:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void LPAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 nestingLevel++; 
			break;
		}
	}
	private void RPAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 if (nestingLevel > 0) nestingLevel--; 
			break;
		}
	}
	private void LBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			 nestingLevel++; 
			break;
		}
	}
	private void RBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			 if (nestingLevel > 0) nestingLevel--; 
			break;
		}
	}
	private void LBRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			 nestingLevel++; 
			break;
		}
	}
	private void RBRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			 if (nestingLevel > 0) nestingLevel--; 
			break;
		}
	}
	private void NEWLINE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:

			        if (nestingLevel > 0) {
			            skip();
			        } else {
			            int la = _input.LA(1);
			            if (la == '\r' || la == '\n' || la == EOF) {
			                skip();
			            } else {
			                String txt = getText();
			                String indentation = "";
			                int last = Math.max(txt.lastIndexOf('\n'), txt.lastIndexOf('\r'));
			                if (last >= 0 && last < txt.length() - 1)
			                    indentation = txt.substring(last + 1);

			                String normalized = indentation //tap=4
			                        .replace("\t", "    ")
			                        .replace("\u00A0", " ")
			                        .replace("\u2007", " ")
			                        .replace("\u202F", " ")
			                        .replace("\u2009", " ");

			                int indentLength = normalized.length();
			                int lastIndent = indentLevels.peek();

			                setText("\n");

			                if (indentLength > lastIndent) {
			                    indentLevels.push(indentLength);
			                    pendingTokens.add(createToken(INDENT, indentation));
			                } else {
			                    while (indentLength < indentLevels.peek()) {
			                        indentLevels.pop();
			                        pendingTokens.add(createToken(DEDENT, ""));
			                    }
			                }
			            }
			        }
			      
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000-\u0129\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0005\u0006m\b\u0006\n\u0006\f\u0006p\t\u0006\u0001\u0007"+
		"\u0003\u0007s\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007w\b\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0005\u0007|\b\u0007\n\u0007\f\u0007\u007f"+
		"\t\u0007\u0003\u0007\u0081\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018"+
		"\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001!"+
		"\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001$\u0001"+
		"%\u0001%\u0001&\u0001&\u0001\'\u0004\'\u00f1\b\'\u000b\'\f\'\u00f2\u0001"+
		"\'\u0001\'\u0004\'\u00f7\b\'\u000b\'\f\'\u00f8\u0003\'\u00fb\b\'\u0001"+
		"(\u0001(\u0001(\u0001(\u0005(\u0101\b(\n(\f(\u0104\t(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0005(\u010b\b(\n(\f(\u010e\t(\u0001(\u0003(\u0111\b("+
		"\u0001)\u0001)\u0005)\u0115\b)\n)\f)\u0118\t)\u0001*\u0004*\u011b\b*\u000b"+
		"*\f*\u011c\u0001*\u0001*\u0001+\u0001+\u0005+\u0123\b+\n+\f+\u0126\t+"+
		"\u0001+\u0001+\u0000\u0000,\u0001\u0003\u0003\u0004\u0005\u0005\u0007"+
		"\u0006\t\u0007\u000b\b\r\u0000\u000f\t\u0011\n\u0013\u000b\u0015\f\u0017"+
		"\r\u0019\u000e\u001b\u000f\u001d\u0010\u001f\u0011!\u0012#\u0013%\u0014"+
		"\'\u0015)\u0016+\u0017-\u0018/\u00191\u001a3\u001b5\u001c7\u001d9\u001e"+
		";\u001f= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-\u0001\u0000\b\u0006\u0000\t\t  "+
		"\u00a0\u00a0\u2007\u2007\u2009\u2009\u202f\u202f\u0002\u0000\n\n\r\r\u0001"+
		"\u000009\u0002\u0000\"\"\\\\\u0002\u0000\'\'\\\\\u0003\u0000AZ__az\u0004"+
		"\u000009AZ__az\u0002\u0000\t\t  \u0137\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"+
		"\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000"+
		"!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001"+
		"\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000"+
		"\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000"+
		"\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u00003"+
		"\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001\u0000"+
		"\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;\u0001\u0000\u0000\u0000"+
		"\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000\u0000\u0000\u0000A"+
		"\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000\u0000\u0000E\u0001\u0000"+
		"\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000I\u0001\u0000\u0000\u0000"+
		"\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001\u0000\u0000\u0000\u0000O"+
		"\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000\u0000\u0000S\u0001\u0000"+
		"\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000W\u0001\u0000\u0000\u0000"+
		"\u0001Y\u0001\u0000\u0000\u0000\u0003\\\u0001\u0000\u0000\u0000\u0005"+
		"_\u0001\u0000\u0000\u0000\u0007b\u0001\u0000\u0000\u0000\te\u0001\u0000"+
		"\u0000\u0000\u000bh\u0001\u0000\u0000\u0000\rn\u0001\u0000\u0000\u0000"+
		"\u000fv\u0001\u0000\u0000\u0000\u0011\u0084\u0001\u0000\u0000\u0000\u0013"+
		"\u0088\u0001\u0000\u0000\u0000\u0015\u008f\u0001\u0000\u0000\u0000\u0017"+
		"\u0092\u0001\u0000\u0000\u0000\u0019\u0097\u0001\u0000\u0000\u0000\u001b"+
		"\u009c\u0001\u0000\u0000\u0000\u001d\u00a0\u0001\u0000\u0000\u0000\u001f"+
		"\u00a3\u0001\u0000\u0000\u0000!\u00aa\u0001\u0000\u0000\u0000#\u00af\u0001"+
		"\u0000\u0000\u0000%\u00b2\u0001\u0000\u0000\u0000\'\u00b7\u0001\u0000"+
		"\u0000\u0000)\u00bd\u0001\u0000\u0000\u0000+\u00c2\u0001\u0000\u0000\u0000"+
		"-\u00c6\u0001\u0000\u0000\u0000/\u00c9\u0001\u0000\u0000\u00001\u00cd"+
		"\u0001\u0000\u0000\u00003\u00cf\u0001\u0000\u0000\u00005\u00d1\u0001\u0000"+
		"\u0000\u00007\u00d3\u0001\u0000\u0000\u00009\u00d5\u0001\u0000\u0000\u0000"+
		";\u00d8\u0001\u0000\u0000\u0000=\u00da\u0001\u0000\u0000\u0000?\u00dd"+
		"\u0001\u0000\u0000\u0000A\u00df\u0001\u0000\u0000\u0000C\u00e1\u0001\u0000"+
		"\u0000\u0000E\u00e4\u0001\u0000\u0000\u0000G\u00e7\u0001\u0000\u0000\u0000"+
		"I\u00e9\u0001\u0000\u0000\u0000K\u00eb\u0001\u0000\u0000\u0000M\u00ed"+
		"\u0001\u0000\u0000\u0000O\u00f0\u0001\u0000\u0000\u0000Q\u0110\u0001\u0000"+
		"\u0000\u0000S\u0112\u0001\u0000\u0000\u0000U\u011a\u0001\u0000\u0000\u0000"+
		"W\u0120\u0001\u0000\u0000\u0000YZ\u0005(\u0000\u0000Z[\u0006\u0000\u0000"+
		"\u0000[\u0002\u0001\u0000\u0000\u0000\\]\u0005)\u0000\u0000]^\u0006\u0001"+
		"\u0001\u0000^\u0004\u0001\u0000\u0000\u0000_`\u0005{\u0000\u0000`a\u0006"+
		"\u0002\u0002\u0000a\u0006\u0001\u0000\u0000\u0000bc\u0005}\u0000\u0000"+
		"cd\u0006\u0003\u0003\u0000d\b\u0001\u0000\u0000\u0000ef\u0005[\u0000\u0000"+
		"fg\u0006\u0004\u0004\u0000g\n\u0001\u0000\u0000\u0000hi\u0005]\u0000\u0000"+
		"ij\u0006\u0005\u0005\u0000j\f\u0001\u0000\u0000\u0000km\u0007\u0000\u0000"+
		"\u0000lk\u0001\u0000\u0000\u0000mp\u0001\u0000\u0000\u0000nl\u0001\u0000"+
		"\u0000\u0000no\u0001\u0000\u0000\u0000o\u000e\u0001\u0000\u0000\u0000"+
		"pn\u0001\u0000\u0000\u0000qs\u0005\r\u0000\u0000rq\u0001\u0000\u0000\u0000"+
		"rs\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tw\u0005\n\u0000\u0000"+
		"uw\u0005\r\u0000\u0000vr\u0001\u0000\u0000\u0000vu\u0001\u0000\u0000\u0000"+
		"wx\u0001\u0000\u0000\u0000x\u0080\u0003\r\u0006\u0000y}\u0005#\u0000\u0000"+
		"z|\b\u0001\u0000\u0000{z\u0001\u0000\u0000\u0000|\u007f\u0001\u0000\u0000"+
		"\u0000}{\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u0081\u0001"+
		"\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u0080y\u0001\u0000\u0000"+
		"\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000"+
		"\u0000\u0082\u0083\u0006\u0007\u0006\u0000\u0083\u0010\u0001\u0000\u0000"+
		"\u0000\u0084\u0085\u0005d\u0000\u0000\u0085\u0086\u0005e\u0000\u0000\u0086"+
		"\u0087\u0005f\u0000\u0000\u0087\u0012\u0001\u0000\u0000\u0000\u0088\u0089"+
		"\u0005r\u0000\u0000\u0089\u008a\u0005e\u0000\u0000\u008a\u008b\u0005t"+
		"\u0000\u0000\u008b\u008c\u0005u\u0000\u0000\u008c\u008d\u0005r\u0000\u0000"+
		"\u008d\u008e\u0005n\u0000\u0000\u008e\u0014\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0005i\u0000\u0000\u0090\u0091\u0005f\u0000\u0000\u0091\u0016\u0001"+
		"\u0000\u0000\u0000\u0092\u0093\u0005e\u0000\u0000\u0093\u0094\u0005l\u0000"+
		"\u0000\u0094\u0095\u0005s\u0000\u0000\u0095\u0096\u0005e\u0000\u0000\u0096"+
		"\u0018\u0001\u0000\u0000\u0000\u0097\u0098\u0005e\u0000\u0000\u0098\u0099"+
		"\u0005l\u0000\u0000\u0099\u009a\u0005i\u0000\u0000\u009a\u009b\u0005f"+
		"\u0000\u0000\u009b\u001a\u0001\u0000\u0000\u0000\u009c\u009d\u0005f\u0000"+
		"\u0000\u009d\u009e\u0005o\u0000\u0000\u009e\u009f\u0005r\u0000\u0000\u009f"+
		"\u001c\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005i\u0000\u0000\u00a1\u00a2"+
		"\u0005n\u0000\u0000\u00a2\u001e\u0001\u0000\u0000\u0000\u00a3\u00a4\u0005"+
		"i\u0000\u0000\u00a4\u00a5\u0005m\u0000\u0000\u00a5\u00a6\u0005p\u0000"+
		"\u0000\u00a6\u00a7\u0005o\u0000\u0000\u00a7\u00a8\u0005r\u0000\u0000\u00a8"+
		"\u00a9\u0005t\u0000\u0000\u00a9 \u0001\u0000\u0000\u0000\u00aa\u00ab\u0005"+
		"f\u0000\u0000\u00ab\u00ac\u0005r\u0000\u0000\u00ac\u00ad\u0005o\u0000"+
		"\u0000\u00ad\u00ae\u0005m\u0000\u0000\u00ae\"\u0001\u0000\u0000\u0000"+
		"\u00af\u00b0\u0005a\u0000\u0000\u00b0\u00b1\u0005s\u0000\u0000\u00b1$"+
		"\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005T\u0000\u0000\u00b3\u00b4\u0005"+
		"r\u0000\u0000\u00b4\u00b5\u0005u\u0000\u0000\u00b5\u00b6\u0005e\u0000"+
		"\u0000\u00b6&\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005F\u0000\u0000\u00b8"+
		"\u00b9\u0005a\u0000\u0000\u00b9\u00ba\u0005l\u0000\u0000\u00ba\u00bb\u0005"+
		"s\u0000\u0000\u00bb\u00bc\u0005e\u0000\u0000\u00bc(\u0001\u0000\u0000"+
		"\u0000\u00bd\u00be\u0005N\u0000\u0000\u00be\u00bf\u0005o\u0000\u0000\u00bf"+
		"\u00c0\u0005n\u0000\u0000\u00c0\u00c1\u0005e\u0000\u0000\u00c1*\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c3\u0005a\u0000\u0000\u00c3\u00c4\u0005n\u0000"+
		"\u0000\u00c4\u00c5\u0005d\u0000\u0000\u00c5,\u0001\u0000\u0000\u0000\u00c6"+
		"\u00c7\u0005o\u0000\u0000\u00c7\u00c8\u0005r\u0000\u0000\u00c8.\u0001"+
		"\u0000\u0000\u0000\u00c9\u00ca\u0005n\u0000\u0000\u00ca\u00cb\u0005o\u0000"+
		"\u0000\u00cb\u00cc\u0005t\u0000\u0000\u00cc0\u0001\u0000\u0000\u0000\u00cd"+
		"\u00ce\u0005+\u0000\u0000\u00ce2\u0001\u0000\u0000\u0000\u00cf\u00d0\u0005"+
		"-\u0000\u0000\u00d04\u0001\u0000\u0000\u0000\u00d1\u00d2\u0005*\u0000"+
		"\u0000\u00d26\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005/\u0000\u0000\u00d4"+
		"8\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005=\u0000\u0000\u00d6\u00d7\u0005"+
		"=\u0000\u0000\u00d7:\u0001\u0000\u0000\u0000\u00d8\u00d9\u0005=\u0000"+
		"\u0000\u00d9<\u0001\u0000\u0000\u0000\u00da\u00db\u0005!\u0000\u0000\u00db"+
		"\u00dc\u0005=\u0000\u0000\u00dc>\u0001\u0000\u0000\u0000\u00dd\u00de\u0005"+
		"<\u0000\u0000\u00de@\u0001\u0000\u0000\u0000\u00df\u00e0\u0005>\u0000"+
		"\u0000\u00e0B\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005<\u0000\u0000\u00e2"+
		"\u00e3\u0005=\u0000\u0000\u00e3D\u0001\u0000\u0000\u0000\u00e4\u00e5\u0005"+
		">\u0000\u0000\u00e5\u00e6\u0005=\u0000\u0000\u00e6F\u0001\u0000\u0000"+
		"\u0000\u00e7\u00e8\u0005,\u0000\u0000\u00e8H\u0001\u0000\u0000\u0000\u00e9"+
		"\u00ea\u0005:\u0000\u0000\u00eaJ\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005"+
		".\u0000\u0000\u00ecL\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005@\u0000"+
		"\u0000\u00eeN\u0001\u0000\u0000\u0000\u00ef\u00f1\u0007\u0002\u0000\u0000"+
		"\u00f0\u00ef\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000\u0000"+
		"\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000"+
		"\u00f3\u00fa\u0001\u0000\u0000\u0000\u00f4\u00f6\u0005.\u0000\u0000\u00f5"+
		"\u00f7\u0007\u0002\u0000\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f7"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f8\u00f6\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f9\u0001\u0000\u0000\u0000\u00f9\u00fb\u0001\u0000\u0000\u0000\u00fa"+
		"\u00f4\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000\u00fb"+
		"P\u0001\u0000\u0000\u0000\u00fc\u0102\u0005\"\u0000\u0000\u00fd\u0101"+
		"\b\u0003\u0000\u0000\u00fe\u00ff\u0005\\\u0000\u0000\u00ff\u0101\t\u0000"+
		"\u0000\u0000\u0100\u00fd\u0001\u0000\u0000\u0000\u0100\u00fe\u0001\u0000"+
		"\u0000\u0000\u0101\u0104\u0001\u0000\u0000\u0000\u0102\u0100\u0001\u0000"+
		"\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000\u0103\u0105\u0001\u0000"+
		"\u0000\u0000\u0104\u0102\u0001\u0000\u0000\u0000\u0105\u0111\u0005\"\u0000"+
		"\u0000\u0106\u010c\u0005\'\u0000\u0000\u0107\u010b\b\u0004\u0000\u0000"+
		"\u0108\u0109\u0005\\\u0000\u0000\u0109\u010b\t\u0000\u0000\u0000\u010a"+
		"\u0107\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010b"+
		"\u010e\u0001\u0000\u0000\u0000\u010c\u010a\u0001\u0000\u0000\u0000\u010c"+
		"\u010d\u0001\u0000\u0000\u0000\u010d\u010f\u0001\u0000\u0000\u0000\u010e"+
		"\u010c\u0001\u0000\u0000\u0000\u010f\u0111\u0005\'\u0000\u0000\u0110\u00fc"+
		"\u0001\u0000\u0000\u0000\u0110\u0106\u0001\u0000\u0000\u0000\u0111R\u0001"+
		"\u0000\u0000\u0000\u0112\u0116\u0007\u0005\u0000\u0000\u0113\u0115\u0007"+
		"\u0006\u0000\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0115\u0118\u0001"+
		"\u0000\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0116\u0117\u0001"+
		"\u0000\u0000\u0000\u0117T\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000"+
		"\u0000\u0000\u0119\u011b\u0007\u0007\u0000\u0000\u011a\u0119\u0001\u0000"+
		"\u0000\u0000\u011b\u011c\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000"+
		"\u0000\u0000\u011c\u011d\u0001\u0000\u0000\u0000\u011d\u011e\u0001\u0000"+
		"\u0000\u0000\u011e\u011f\u0006*\u0007\u0000\u011fV\u0001\u0000\u0000\u0000"+
		"\u0120\u0124\u0005#\u0000\u0000\u0121\u0123\b\u0001\u0000\u0000\u0122"+
		"\u0121\u0001\u0000\u0000\u0000\u0123\u0126\u0001\u0000\u0000\u0000\u0124"+
		"\u0122\u0001\u0000\u0000\u0000\u0124\u0125\u0001\u0000\u0000\u0000\u0125"+
		"\u0127\u0001\u0000\u0000\u0000\u0126\u0124\u0001\u0000\u0000\u0000\u0127"+
		"\u0128\u0006+\u0007\u0000\u0128X\u0001\u0000\u0000\u0000\u0011\u0000n"+
		"rv}\u0080\u00f2\u00f8\u00fa\u0100\u0102\u010a\u010c\u0110\u0116\u011c"+
		"\u0124\b\u0001\u0000\u0000\u0001\u0001\u0001\u0001\u0002\u0002\u0001\u0003"+
		"\u0003\u0001\u0004\u0004\u0001\u0005\u0005\u0001\u0007\u0006\u0006\u0000"+
		"\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}