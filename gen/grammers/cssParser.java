// Generated from grammers/cssParser.g4 by ANTLR 4.13.2
package grammers;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class cssParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MEDIA_OPEN=1, OPENBRACE=2, CLOSEBRACE=3, PSEUDO_ELEMENT=4, PSEUDO_CLASS=5, 
		ID_SELECTOR=6, CLASS_SELECTOR=7, TAG_SELECTOR=8, COMMA_CSS=9, CSS_WS=10, 
		CSS_COMMENT=11, IDENT=12, STRING_CSS=13, NUMBER=14, COLOR=15, LPAREN_VALUE=16, 
		RPAREN_VALUE=17, SEMICOLON_CSS=18, COLON_CSS=19, WS=20, COMMENT=21;
	public static final int
		RULE_stylesheet = 0, RULE_mediaRule = 1, RULE_ruleset = 2, RULE_selector = 3, 
		RULE_complexSelector = 4, RULE_simpleSelector = 5, RULE_properties = 6, 
		RULE_value = 7, RULE_valueFragment = 8, RULE_functionCall = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"stylesheet", "mediaRule", "ruleset", "selector", "complexSelector", 
			"simpleSelector", "properties", "value", "valueFragment", "functionCall"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'{'", null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "'('", "')'", "';'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "MEDIA_OPEN", "OPENBRACE", "CLOSEBRACE", "PSEUDO_ELEMENT", "PSEUDO_CLASS", 
			"ID_SELECTOR", "CLASS_SELECTOR", "TAG_SELECTOR", "COMMA_CSS", "CSS_WS", 
			"CSS_COMMENT", "IDENT", "STRING_CSS", "NUMBER", "COLOR", "LPAREN_VALUE", 
			"RPAREN_VALUE", "SEMICOLON_CSS", "COLON_CSS", "WS", "COMMENT"
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

	@Override
	public String getGrammarFileName() { return "cssParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public cssParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StylesheetContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(cssParser.EOF, 0); }
		public List<RulesetContext> ruleset() {
			return getRuleContexts(RulesetContext.class);
		}
		public RulesetContext ruleset(int i) {
			return getRuleContext(RulesetContext.class,i);
		}
		public List<MediaRuleContext> mediaRule() {
			return getRuleContexts(MediaRuleContext.class);
		}
		public MediaRuleContext mediaRule(int i) {
			return getRuleContext(MediaRuleContext.class,i);
		}
		public StylesheetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stylesheet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterStylesheet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitStylesheet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitStylesheet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StylesheetContext stylesheet() throws RecognitionException {
		StylesheetContext _localctx = new StylesheetContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_stylesheet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 502L) != 0)) {
				{
				setState(22);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case OPENBRACE:
				case PSEUDO_ELEMENT:
				case PSEUDO_CLASS:
				case ID_SELECTOR:
				case CLASS_SELECTOR:
				case TAG_SELECTOR:
					{
					setState(20);
					ruleset();
					}
					break;
				case MEDIA_OPEN:
					{
					setState(21);
					mediaRule();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(27);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MediaRuleContext extends ParserRuleContext {
		public TerminalNode MEDIA_OPEN() { return getToken(cssParser.MEDIA_OPEN, 0); }
		public TerminalNode CLOSEBRACE() { return getToken(cssParser.CLOSEBRACE, 0); }
		public List<RulesetContext> ruleset() {
			return getRuleContexts(RulesetContext.class);
		}
		public RulesetContext ruleset(int i) {
			return getRuleContext(RulesetContext.class,i);
		}
		public MediaRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mediaRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterMediaRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitMediaRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitMediaRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MediaRuleContext mediaRule() throws RecognitionException {
		MediaRuleContext _localctx = new MediaRuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mediaRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(MEDIA_OPEN);
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 500L) != 0)) {
				{
				{
				setState(30);
				ruleset();
				}
				}
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(36);
			match(CLOSEBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RulesetContext extends ParserRuleContext {
		public TerminalNode OPENBRACE() { return getToken(cssParser.OPENBRACE, 0); }
		public TerminalNode CLOSEBRACE() { return getToken(cssParser.CLOSEBRACE, 0); }
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public List<PropertiesContext> properties() {
			return getRuleContexts(PropertiesContext.class);
		}
		public PropertiesContext properties(int i) {
			return getRuleContext(PropertiesContext.class,i);
		}
		public RulesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterRuleset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitRuleset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitRuleset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesetContext ruleset() throws RecognitionException {
		RulesetContext _localctx = new RulesetContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ruleset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 496L) != 0)) {
				{
				setState(38);
				selector();
				}
			}

			setState(41);
			match(OPENBRACE);
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENT) {
				{
				{
				setState(42);
				properties();
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(48);
			match(CLOSEBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectorContext extends ParserRuleContext {
		public List<ComplexSelectorContext> complexSelector() {
			return getRuleContexts(ComplexSelectorContext.class);
		}
		public ComplexSelectorContext complexSelector(int i) {
			return getRuleContext(ComplexSelectorContext.class,i);
		}
		public List<TerminalNode> COMMA_CSS() { return getTokens(cssParser.COMMA_CSS); }
		public TerminalNode COMMA_CSS(int i) {
			return getToken(cssParser.COMMA_CSS, i);
		}
		public SelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorContext selector() throws RecognitionException {
		SelectorContext _localctx = new SelectorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_selector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			complexSelector();
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA_CSS) {
				{
				{
				setState(51);
				match(COMMA_CSS);
				setState(52);
				complexSelector();
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComplexSelectorContext extends ParserRuleContext {
		public List<SimpleSelectorContext> simpleSelector() {
			return getRuleContexts(SimpleSelectorContext.class);
		}
		public SimpleSelectorContext simpleSelector(int i) {
			return getRuleContext(SimpleSelectorContext.class,i);
		}
		public ComplexSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterComplexSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitComplexSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitComplexSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexSelectorContext complexSelector() throws RecognitionException {
		ComplexSelectorContext _localctx = new ComplexSelectorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_complexSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(58);
				simpleSelector();
				}
				}
				setState(61); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 496L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleSelectorContext extends ParserRuleContext {
		public TerminalNode TAG_SELECTOR() { return getToken(cssParser.TAG_SELECTOR, 0); }
		public TerminalNode CLASS_SELECTOR() { return getToken(cssParser.CLASS_SELECTOR, 0); }
		public TerminalNode ID_SELECTOR() { return getToken(cssParser.ID_SELECTOR, 0); }
		public TerminalNode PSEUDO_ELEMENT() { return getToken(cssParser.PSEUDO_ELEMENT, 0); }
		public TerminalNode PSEUDO_CLASS() { return getToken(cssParser.PSEUDO_CLASS, 0); }
		public SimpleSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterSimpleSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitSimpleSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitSimpleSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleSelectorContext simpleSelector() throws RecognitionException {
		SimpleSelectorContext _localctx = new SimpleSelectorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_simpleSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 496L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertiesContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(cssParser.IDENT, 0); }
		public TerminalNode COLON_CSS() { return getToken(cssParser.COLON_CSS, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode SEMICOLON_CSS() { return getToken(cssParser.SEMICOLON_CSS, 0); }
		public PropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_properties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertiesContext properties() throws RecognitionException {
		PropertiesContext _localctx = new PropertiesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_properties);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(IDENT);
			setState(66);
			match(COLON_CSS);
			setState(67);
			value();
			setState(68);
			match(SEMICOLON_CSS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public List<ValueFragmentContext> valueFragment() {
			return getRuleContexts(ValueFragmentContext.class);
		}
		public ValueFragmentContext valueFragment(int i) {
			return getRuleContext(ValueFragmentContext.class,i);
		}
		public List<TerminalNode> COMMA_CSS() { return getTokens(cssParser.COMMA_CSS); }
		public TerminalNode COMMA_CSS(int i) {
			return getToken(cssParser.COMMA_CSS, i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(70);
				valueFragment();
				}
				}
				setState(73); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 61440L) != 0) );
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA_CSS) {
				{
				{
				setState(75);
				match(COMMA_CSS);
				setState(77); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(76);
					valueFragment();
					}
					}
					setState(79); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 61440L) != 0) );
				}
				}
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueFragmentContext extends ParserRuleContext {
		public TerminalNode STRING_CSS() { return getToken(cssParser.STRING_CSS, 0); }
		public TerminalNode NUMBER() { return getToken(cssParser.NUMBER, 0); }
		public TerminalNode COLOR() { return getToken(cssParser.COLOR, 0); }
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(cssParser.IDENT, 0); }
		public ValueFragmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueFragment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterValueFragment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitValueFragment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitValueFragment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueFragmentContext valueFragment() throws RecognitionException {
		ValueFragmentContext _localctx = new ValueFragmentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_valueFragment);
		try {
			setState(91);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(86);
				match(STRING_CSS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(87);
				match(NUMBER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(88);
				match(COLOR);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(89);
				functionCall();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(90);
				match(IDENT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(cssParser.IDENT, 0); }
		public TerminalNode LPAREN_VALUE() { return getToken(cssParser.LPAREN_VALUE, 0); }
		public TerminalNode RPAREN_VALUE() { return getToken(cssParser.RPAREN_VALUE, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cssParserListener ) ((cssParserListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cssParserVisitor ) return ((cssParserVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(IDENT);
			setState(94);
			match(LPAREN_VALUE);
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 61440L) != 0)) {
				{
				setState(95);
				value();
				}
			}

			setState(98);
			match(RPAREN_VALUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0015e\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0001\u0000\u0001\u0000\u0005\u0000\u0017\b"+
		"\u0000\n\u0000\f\u0000\u001a\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0005\u0001 \b\u0001\n\u0001\f\u0001#\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0003\u0002(\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0005\u0002,\b\u0002\n\u0002\f\u0002/\t\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u00036\b\u0003\n\u0003\f\u0003"+
		"9\t\u0003\u0001\u0004\u0004\u0004<\b\u0004\u000b\u0004\f\u0004=\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0004\u0007H\b\u0007\u000b\u0007\f\u0007I\u0001\u0007"+
		"\u0001\u0007\u0004\u0007N\b\u0007\u000b\u0007\f\u0007O\u0005\u0007R\b"+
		"\u0007\n\u0007\f\u0007U\t\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0003\b\\\b\b\u0001\t\u0001\t\u0001\t\u0003\ta\b\t\u0001\t\u0001\t"+
		"\u0001\t\u0000\u0000\n\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0000\u0001\u0001\u0000\u0004\bi\u0000\u0018\u0001\u0000\u0000\u0000"+
		"\u0002\u001d\u0001\u0000\u0000\u0000\u0004\'\u0001\u0000\u0000\u0000\u0006"+
		"2\u0001\u0000\u0000\u0000\b;\u0001\u0000\u0000\u0000\n?\u0001\u0000\u0000"+
		"\u0000\fA\u0001\u0000\u0000\u0000\u000eG\u0001\u0000\u0000\u0000\u0010"+
		"[\u0001\u0000\u0000\u0000\u0012]\u0001\u0000\u0000\u0000\u0014\u0017\u0003"+
		"\u0004\u0002\u0000\u0015\u0017\u0003\u0002\u0001\u0000\u0016\u0014\u0001"+
		"\u0000\u0000\u0000\u0016\u0015\u0001\u0000\u0000\u0000\u0017\u001a\u0001"+
		"\u0000\u0000\u0000\u0018\u0016\u0001\u0000\u0000\u0000\u0018\u0019\u0001"+
		"\u0000\u0000\u0000\u0019\u001b\u0001\u0000\u0000\u0000\u001a\u0018\u0001"+
		"\u0000\u0000\u0000\u001b\u001c\u0005\u0000\u0000\u0001\u001c\u0001\u0001"+
		"\u0000\u0000\u0000\u001d!\u0005\u0001\u0000\u0000\u001e \u0003\u0004\u0002"+
		"\u0000\u001f\u001e\u0001\u0000\u0000\u0000 #\u0001\u0000\u0000\u0000!"+
		"\u001f\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"$\u0001\u0000"+
		"\u0000\u0000#!\u0001\u0000\u0000\u0000$%\u0005\u0003\u0000\u0000%\u0003"+
		"\u0001\u0000\u0000\u0000&(\u0003\u0006\u0003\u0000\'&\u0001\u0000\u0000"+
		"\u0000\'(\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)-\u0005\u0002"+
		"\u0000\u0000*,\u0003\f\u0006\u0000+*\u0001\u0000\u0000\u0000,/\u0001\u0000"+
		"\u0000\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000.0\u0001"+
		"\u0000\u0000\u0000/-\u0001\u0000\u0000\u000001\u0005\u0003\u0000\u0000"+
		"1\u0005\u0001\u0000\u0000\u000027\u0003\b\u0004\u000034\u0005\t\u0000"+
		"\u000046\u0003\b\u0004\u000053\u0001\u0000\u0000\u000069\u0001\u0000\u0000"+
		"\u000075\u0001\u0000\u0000\u000078\u0001\u0000\u0000\u00008\u0007\u0001"+
		"\u0000\u0000\u000097\u0001\u0000\u0000\u0000:<\u0003\n\u0005\u0000;:\u0001"+
		"\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000"+
		"=>\u0001\u0000\u0000\u0000>\t\u0001\u0000\u0000\u0000?@\u0007\u0000\u0000"+
		"\u0000@\u000b\u0001\u0000\u0000\u0000AB\u0005\f\u0000\u0000BC\u0005\u0013"+
		"\u0000\u0000CD\u0003\u000e\u0007\u0000DE\u0005\u0012\u0000\u0000E\r\u0001"+
		"\u0000\u0000\u0000FH\u0003\u0010\b\u0000GF\u0001\u0000\u0000\u0000HI\u0001"+
		"\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000"+
		"JS\u0001\u0000\u0000\u0000KM\u0005\t\u0000\u0000LN\u0003\u0010\b\u0000"+
		"ML\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000\u0000OM\u0001\u0000\u0000"+
		"\u0000OP\u0001\u0000\u0000\u0000PR\u0001\u0000\u0000\u0000QK\u0001\u0000"+
		"\u0000\u0000RU\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000ST\u0001"+
		"\u0000\u0000\u0000T\u000f\u0001\u0000\u0000\u0000US\u0001\u0000\u0000"+
		"\u0000V\\\u0005\r\u0000\u0000W\\\u0005\u000e\u0000\u0000X\\\u0005\u000f"+
		"\u0000\u0000Y\\\u0003\u0012\t\u0000Z\\\u0005\f\u0000\u0000[V\u0001\u0000"+
		"\u0000\u0000[W\u0001\u0000\u0000\u0000[X\u0001\u0000\u0000\u0000[Y\u0001"+
		"\u0000\u0000\u0000[Z\u0001\u0000\u0000\u0000\\\u0011\u0001\u0000\u0000"+
		"\u0000]^\u0005\f\u0000\u0000^`\u0005\u0010\u0000\u0000_a\u0003\u000e\u0007"+
		"\u0000`_\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000ab\u0001\u0000"+
		"\u0000\u0000bc\u0005\u0011\u0000\u0000c\u0013\u0001\u0000\u0000\u0000"+
		"\f\u0016\u0018!\'-7=IOS[`";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}