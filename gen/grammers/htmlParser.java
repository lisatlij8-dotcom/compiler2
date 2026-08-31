// Generated from grammers/htmlParser.g4 by ANTLR 4.13.2
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
public class htmlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, COMMENT=2, EXPR_START=3, TAG_START=4, COMMENT_START=5, HTML_DOCUMENT=6, 
		STYLE_OPEN=7, OPEN_TAG=8, HTML_TEXT=9, TEXT=10, TAG_WS=11, CLOSE_TAG=12, 
		CLOSE_SLASH_TAG=13, TAG_SLASH=14, VOID_TAG_NAME=15, TAG_NAME=16, ATTR_NAME=17, 
		TAG_EQUALS=18, ATTRIBUTE_VALUE=19, ATTRIBUTE=20, ATTR_WS=21, STYLE_CLOSE=22, 
		CSS_WS=23, CSS_COMMENT=24, OPENBRACE=25, CSS_SELECTOR=26, PROPERTY=27, 
		VALUE=28, SEMICOLON_CSS=29, COLON_CSS=30, COMMA_CSS=31, CLOSEBRACE=32, 
		CSS_PROP_WS=33, EXPR_END=34, DOT=35, COMMA=36, COLON=37, PIPE=38, LPAREN=39, 
		RPAREN=40, LBRACK=41, RBRACK=42, LBRACE=43, RBRACE=44, PLUS=45, MINUS=46, 
		MULT=47, DIV=48, MOD=49, POW=50, EQ=51, NE=52, ASSIGN=53, LT=54, GT=55, 
		LE=56, GE=57, AND=58, OR=59, NOT=60, IN=61, IS=62, NONE=63, TRUE=64, FALSE=65, 
		ID=66, INT=67, FLOAT=68, STRING=69, EXPR_WS=70, JINJA_TAG_WS=71, TAG_END=72, 
		IF=73, ELIF=74, ELSE=75, ENDIF=76, SET=77, INCLUDE=78, EXTENDS=79, FOR=80, 
		ENDFOR=81, COMMENT_END=82, COMMENT_TEXT=83;
	public static final int
		RULE_htmlDocument = 0, RULE_htmlElement = 1, RULE_htmlTag = 2, RULE_htmlAttribute = 3, 
		RULE_cssStyle = 4, RULE_stylesheet = 5, RULE_ruleset = 6, RULE_properties = 7, 
		RULE_value = 8, RULE_jinjaExpression = 9, RULE_jinjaTag = 10, RULE_jinjaComment = 11, 
		RULE_expr = 12, RULE_literal = 13, RULE_jinjaSingleTag = 14, RULE_singleJinjaTagContent = 15, 
		RULE_firstBlockJinjaTagContent = 16, RULE_endBlockJinjaTagContent = 17, 
		RULE_jinjaBlock = 18, RULE_jinjaForBlock = 19, RULE_jinjaIfBlock = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"htmlDocument", "htmlElement", "htmlTag", "htmlAttribute", "cssStyle", 
			"stylesheet", "ruleset", "properties", "value", "jinjaExpression", "jinjaTag", 
			"jinjaComment", "expr", "literal", "jinjaSingleTag", "singleJinjaTagContent", 
			"firstBlockJinjaTagContent", "endBlockJinjaTagContent", "jinjaBlock", 
			"jinjaForBlock", "jinjaIfBlock"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'{{'", "'{%'", "'{#'", "'<!DOCTYPE html>'", "'<style>'", 
			null, null, null, null, null, "'/>'", null, null, null, null, null, null, 
			null, null, "'</style>'", null, null, null, null, null, null, "';'", 
			null, null, null, null, "'}}'", null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "'%}'", "'if'", "'elif'", "'else'", 
			"'endif'", "'set'", "'include'", "'extends'", "'for'", "'endfor'", "'#}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "COMMENT", "EXPR_START", "TAG_START", "COMMENT_START", "HTML_DOCUMENT", 
			"STYLE_OPEN", "OPEN_TAG", "HTML_TEXT", "TEXT", "TAG_WS", "CLOSE_TAG", 
			"CLOSE_SLASH_TAG", "TAG_SLASH", "VOID_TAG_NAME", "TAG_NAME", "ATTR_NAME", 
			"TAG_EQUALS", "ATTRIBUTE_VALUE", "ATTRIBUTE", "ATTR_WS", "STYLE_CLOSE", 
			"CSS_WS", "CSS_COMMENT", "OPENBRACE", "CSS_SELECTOR", "PROPERTY", "VALUE", 
			"SEMICOLON_CSS", "COLON_CSS", "COMMA_CSS", "CLOSEBRACE", "CSS_PROP_WS", 
			"EXPR_END", "DOT", "COMMA", "COLON", "PIPE", "LPAREN", "RPAREN", "LBRACK", 
			"RBRACK", "LBRACE", "RBRACE", "PLUS", "MINUS", "MULT", "DIV", "MOD", 
			"POW", "EQ", "NE", "ASSIGN", "LT", "GT", "LE", "GE", "AND", "OR", "NOT", 
			"IN", "IS", "NONE", "TRUE", "FALSE", "ID", "INT", "FLOAT", "STRING", 
			"EXPR_WS", "JINJA_TAG_WS", "TAG_END", "IF", "ELIF", "ELSE", "ENDIF", 
			"SET", "INCLUDE", "EXTENDS", "FOR", "ENDFOR", "COMMENT_END", "COMMENT_TEXT"
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
	public String getGrammarFileName() { return "htmlParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public htmlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HtmlDocumentContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(htmlParser.EOF, 0); }
		public TerminalNode HTML_DOCUMENT() { return getToken(htmlParser.HTML_DOCUMENT, 0); }
		public List<HtmlElementContext> htmlElement() {
			return getRuleContexts(HtmlElementContext.class);
		}
		public HtmlElementContext htmlElement(int i) {
			return getRuleContext(HtmlElementContext.class,i);
		}
		public HtmlDocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlDocument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterHtmlDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitHtmlDocument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitHtmlDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlDocumentContext htmlDocument() throws RecognitionException {
		HtmlDocumentContext _localctx = new HtmlDocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_htmlDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HTML_DOCUMENT) {
				{
				setState(42);
				match(HTML_DOCUMENT);
				}
			}

			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 952L) != 0)) {
				{
				{
				setState(45);
				htmlElement();
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(51);
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
	public static class HtmlElementContext extends ParserRuleContext {
		public HtmlElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElement; }
	 
		public HtmlElementContext() { }
		public void copyFrom(HtmlElementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Jinja_TagContext extends HtmlElementContext {
		public JinjaTagContext jinjaTag() {
			return getRuleContext(JinjaTagContext.class,0);
		}
		public Jinja_TagContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinja_Tag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinja_Tag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinja_Tag(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Tag_htmlContext extends HtmlElementContext {
		public HtmlTagContext htmlTag() {
			return getRuleContext(HtmlTagContext.class,0);
		}
		public Tag_htmlContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterTag_html(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitTag_html(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitTag_html(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaExprContext extends HtmlElementContext {
		public JinjaExpressionContext jinjaExpression() {
			return getRuleContext(JinjaExpressionContext.class,0);
		}
		public JinjaExprContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaCommContext extends HtmlElementContext {
		public JinjaCommentContext jinjaComment() {
			return getRuleContext(JinjaCommentContext.class,0);
		}
		public JinjaCommContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaComm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaComm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaComm(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TextContext extends HtmlElementContext {
		public TerminalNode HTML_TEXT() { return getToken(htmlParser.HTML_TEXT, 0); }
		public TextContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitText(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CssStyContext extends HtmlElementContext {
		public CssStyleContext cssStyle() {
			return getRuleContext(CssStyleContext.class,0);
		}
		public CssStyContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterCssSty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitCssSty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitCssSty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlElementContext htmlElement() throws RecognitionException {
		HtmlElementContext _localctx = new HtmlElementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_htmlElement);
		try {
			setState(59);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPEN_TAG:
				_localctx = new Tag_htmlContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				htmlTag();
				}
				break;
			case STYLE_OPEN:
				_localctx = new CssStyContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(54);
				cssStyle();
				}
				break;
			case EXPR_START:
				_localctx = new JinjaExprContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				jinjaExpression();
				}
				break;
			case TAG_START:
				_localctx = new Jinja_TagContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(56);
				jinjaTag();
				}
				break;
			case COMMENT_START:
				_localctx = new JinjaCommContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				jinjaComment();
				}
				break;
			case HTML_TEXT:
				_localctx = new TextContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(58);
				match(HTML_TEXT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class HtmlTagContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(htmlParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(htmlParser.OPEN_TAG, i);
		}
		public TerminalNode VOID_TAG_NAME() { return getToken(htmlParser.VOID_TAG_NAME, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(htmlParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(htmlParser.CLOSE_TAG, i);
		}
		public TerminalNode CLOSE_SLASH_TAG() { return getToken(htmlParser.CLOSE_SLASH_TAG, 0); }
		public List<HtmlAttributeContext> htmlAttribute() {
			return getRuleContexts(HtmlAttributeContext.class);
		}
		public HtmlAttributeContext htmlAttribute(int i) {
			return getRuleContext(HtmlAttributeContext.class,i);
		}
		public List<TerminalNode> TAG_NAME() { return getTokens(htmlParser.TAG_NAME); }
		public TerminalNode TAG_NAME(int i) {
			return getToken(htmlParser.TAG_NAME, i);
		}
		public List<HtmlElementContext> htmlElement() {
			return getRuleContexts(HtmlElementContext.class);
		}
		public HtmlElementContext htmlElement(int i) {
			return getRuleContext(HtmlElementContext.class,i);
		}
		public TerminalNode TAG_SLASH() { return getToken(htmlParser.TAG_SLASH, 0); }
		public HtmlTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterHtmlTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitHtmlTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitHtmlTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlTagContext htmlTag() throws RecognitionException {
		HtmlTagContext _localctx = new HtmlTagContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_htmlTag);
		int _la;
		try {
			int _alt;
			setState(100);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				match(OPEN_TAG);
				setState(62);
				match(VOID_TAG_NAME);
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ATTR_NAME) {
					{
					{
					setState(63);
					htmlAttribute();
					}
					}
					setState(68);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(69);
				_la = _input.LA(1);
				if ( !(_la==CLOSE_TAG || _la==CLOSE_SLASH_TAG) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
				match(OPEN_TAG);
				setState(71);
				match(TAG_NAME);
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ATTR_NAME) {
					{
					{
					setState(72);
					htmlAttribute();
					}
					}
					setState(77);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(78);
				match(CLOSE_TAG);
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(79);
						htmlElement();
						}
						} 
					}
					setState(84);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				}
				setState(89);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(85);
					match(OPEN_TAG);
					setState(86);
					match(TAG_SLASH);
					setState(87);
					match(TAG_NAME);
					setState(88);
					match(CLOSE_TAG);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(91);
				match(OPEN_TAG);
				setState(92);
				match(TAG_NAME);
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ATTR_NAME) {
					{
					{
					setState(93);
					htmlAttribute();
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(99);
				match(CLOSE_SLASH_TAG);
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
	public static class HtmlAttributeContext extends ParserRuleContext {
		public TerminalNode ATTR_NAME() { return getToken(htmlParser.ATTR_NAME, 0); }
		public TerminalNode TAG_EQUALS() { return getToken(htmlParser.TAG_EQUALS, 0); }
		public TerminalNode ATTRIBUTE_VALUE() { return getToken(htmlParser.ATTRIBUTE_VALUE, 0); }
		public HtmlAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterHtmlAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitHtmlAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitHtmlAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlAttributeContext htmlAttribute() throws RecognitionException {
		HtmlAttributeContext _localctx = new HtmlAttributeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_htmlAttribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(ATTR_NAME);
			setState(105);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TAG_EQUALS) {
				{
				setState(103);
				match(TAG_EQUALS);
				setState(104);
				match(ATTRIBUTE_VALUE);
				}
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
	public static class CssStyleContext extends ParserRuleContext {
		public TerminalNode STYLE_OPEN() { return getToken(htmlParser.STYLE_OPEN, 0); }
		public StylesheetContext stylesheet() {
			return getRuleContext(StylesheetContext.class,0);
		}
		public TerminalNode STYLE_CLOSE() { return getToken(htmlParser.STYLE_CLOSE, 0); }
		public CssStyleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssStyle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterCssStyle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitCssStyle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitCssStyle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssStyleContext cssStyle() throws RecognitionException {
		CssStyleContext _localctx = new CssStyleContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_cssStyle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(STYLE_OPEN);
			setState(108);
			stylesheet();
			setState(109);
			match(STYLE_CLOSE);
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
	public static class StylesheetContext extends ParserRuleContext {
		public List<RulesetContext> ruleset() {
			return getRuleContexts(RulesetContext.class);
		}
		public RulesetContext ruleset(int i) {
			return getRuleContext(RulesetContext.class,i);
		}
		public StylesheetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stylesheet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterStylesheet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitStylesheet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitStylesheet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StylesheetContext stylesheet() throws RecognitionException {
		StylesheetContext _localctx = new StylesheetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_stylesheet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CSS_SELECTOR) {
				{
				{
				setState(111);
				ruleset();
				}
				}
				setState(116);
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
	public static class RulesetContext extends ParserRuleContext {
		public TerminalNode CSS_SELECTOR() { return getToken(htmlParser.CSS_SELECTOR, 0); }
		public TerminalNode OPENBRACE() { return getToken(htmlParser.OPENBRACE, 0); }
		public TerminalNode CLOSEBRACE() { return getToken(htmlParser.CLOSEBRACE, 0); }
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
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterRuleset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitRuleset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitRuleset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesetContext ruleset() throws RecognitionException {
		RulesetContext _localctx = new RulesetContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_ruleset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(CSS_SELECTOR);
			setState(118);
			match(OPENBRACE);
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PROPERTY) {
				{
				{
				setState(119);
				properties();
				}
				}
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(125);
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
	public static class PropertiesContext extends ParserRuleContext {
		public TerminalNode PROPERTY() { return getToken(htmlParser.PROPERTY, 0); }
		public TerminalNode COLON_CSS() { return getToken(htmlParser.COLON_CSS, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode SEMICOLON_CSS() { return getToken(htmlParser.SEMICOLON_CSS, 0); }
		public PropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_properties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertiesContext properties() throws RecognitionException {
		PropertiesContext _localctx = new PropertiesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_properties);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(PROPERTY);
			setState(128);
			match(COLON_CSS);
			setState(129);
			value();
			setState(130);
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
		public List<TerminalNode> VALUE() { return getTokens(htmlParser.VALUE); }
		public TerminalNode VALUE(int i) {
			return getToken(htmlParser.VALUE, i);
		}
		public List<TerminalNode> COMMA_CSS() { return getTokens(htmlParser.COMMA_CSS); }
		public TerminalNode COMMA_CSS(int i) {
			return getToken(htmlParser.COMMA_CSS, i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(VALUE);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA_CSS) {
				{
				{
				setState(133);
				match(COMMA_CSS);
				setState(134);
				match(VALUE);
				}
				}
				setState(139);
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
	public static class JinjaExpressionContext extends ParserRuleContext {
		public TerminalNode EXPR_START() { return getToken(htmlParser.EXPR_START, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EXPR_END() { return getToken(htmlParser.EXPR_END, 0); }
		public JinjaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExpressionContext jinjaExpression() throws RecognitionException {
		JinjaExpressionContext _localctx = new JinjaExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_jinjaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(EXPR_START);
			setState(141);
			expr(0);
			setState(142);
			match(EXPR_END);
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
	public static class JinjaTagContext extends ParserRuleContext {
		public JinjaSingleTagContext jinjaSingleTag() {
			return getRuleContext(JinjaSingleTagContext.class,0);
		}
		public JinjaBlockContext jinjaBlock() {
			return getRuleContext(JinjaBlockContext.class,0);
		}
		public JinjaTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaTagContext jinjaTag() throws RecognitionException {
		JinjaTagContext _localctx = new JinjaTagContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_jinjaTag);
		try {
			setState(146);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(144);
				jinjaSingleTag();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				jinjaBlock();
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
	public static class JinjaCommentContext extends ParserRuleContext {
		public TerminalNode COMMENT_START() { return getToken(htmlParser.COMMENT_START, 0); }
		public TerminalNode COMMENT_END() { return getToken(htmlParser.COMMENT_END, 0); }
		public TerminalNode COMMENT_TEXT() { return getToken(htmlParser.COMMENT_TEXT, 0); }
		public JinjaCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaCommentContext jinjaComment() throws RecognitionException {
		JinjaCommentContext _localctx = new JinjaCommentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_jinjaComment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(COMMENT_START);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT_TEXT) {
				{
				setState(149);
				match(COMMENT_TEXT);
				}
			}

			setState(152);
			match(COMMENT_END);
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
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PowerExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode POW() { return getToken(htmlParser.POW, 0); }
		public PowerExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterPowerExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitPowerExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitPowerExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddSubExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(htmlParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(htmlParser.MINUS, 0); }
		public AddSubExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterAddSubExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitAddSubExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitAddSubExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IN() { return getToken(htmlParser.IN, 0); }
		public InExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterInExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitInExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitInExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OR() { return getToken(htmlParser.OR, 0); }
		public OrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubscriptionExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LBRACK() { return getToken(htmlParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(htmlParser.RBRACK, 0); }
		public SubscriptionExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterSubscriptionExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitSubscriptionExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitSubscriptionExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivModExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULT() { return getToken(htmlParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(htmlParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(htmlParser.MOD, 0); }
		public MulDivModExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterMulDivModExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitMulDivModExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitMulDivModExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LT() { return getToken(htmlParser.LT, 0); }
		public TerminalNode GT() { return getToken(htmlParser.GT, 0); }
		public TerminalNode LE() { return getToken(htmlParser.LE, 0); }
		public TerminalNode GE() { return getToken(htmlParser.GE, 0); }
		public ComparisonExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterComparisonExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitComparisonExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FilterExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PIPE() { return getToken(htmlParser.PIPE, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(htmlParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(htmlParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(htmlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(htmlParser.COMMA, i);
		}
		public FilterExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AttributeExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(htmlParser.DOT, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public AttributeExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterAttributeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitAttributeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitAttributeExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExprContext extends ExprContext {
		public TerminalNode LPAREN() { return getToken(htmlParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(htmlParser.RPAREN, 0); }
		public ParenExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterParenExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitParenExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitParenExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotExprContext extends ExprContext {
		public TerminalNode NOT() { return getToken(htmlParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterNotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitNotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryMinusExprContext extends ExprContext {
		public TerminalNode MINUS() { return getToken(htmlParser.MINUS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnaryMinusExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterUnaryMinusExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitUnaryMinusExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitUnaryMinusExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExprContext extends ExprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterLiteralExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitLiteralExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CallExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(htmlParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(htmlParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(htmlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(htmlParser.COMMA, i);
		}
		public CallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IsExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IS() { return getToken(htmlParser.IS, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(htmlParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(htmlParser.RPAREN, 0); }
		public IsExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterIsExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitIsExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitIsExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode EQ() { return getToken(htmlParser.EQ, 0); }
		public TerminalNode NE() { return getToken(htmlParser.NE, 0); }
		public EqualityExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterEqualityExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitEqualityExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitEqualityExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierExprContext extends ExprContext {
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public IdentifierExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterIdentifierExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitIdentifierExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitIdentifierExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(htmlParser.AND, 0); }
		public AndExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACK:
			case LBRACE:
			case NONE:
			case TRUE:
			case FALSE:
			case INT:
			case FLOAT:
			case STRING:
				{
				_localctx = new LiteralExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(155);
				literal();
				}
				break;
			case ID:
				{
				_localctx = new IdentifierExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156);
				match(ID);
				}
				break;
			case MINUS:
				{
				_localctx = new UnaryMinusExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(157);
				match(MINUS);
				setState(158);
				expr(12);
				}
				break;
			case NOT:
				{
				_localctx = new NotExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(159);
				match(NOT);
				setState(160);
				expr(11);
				}
				break;
			case LPAREN:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(161);
				match(LPAREN);
				setState(162);
				expr(0);
				setState(163);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(241);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(239);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new PowerExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(167);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(168);
						match(POW);
						setState(169);
						expr(11);
						}
						break;
					case 2:
						{
						_localctx = new MulDivModExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(170);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(171);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 985162418487296L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(172);
						expr(10);
						}
						break;
					case 3:
						{
						_localctx = new AddSubExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(173);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(174);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(175);
						expr(9);
						}
						break;
					case 4:
						{
						_localctx = new ComparisonExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(176);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(177);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 270215977642229760L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(178);
						expr(8);
						}
						break;
					case 5:
						{
						_localctx = new EqualityExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(179);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(180);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NE) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(181);
						expr(7);
						}
						break;
					case 6:
						{
						_localctx = new AndExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(182);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(183);
						match(AND);
						setState(184);
						expr(6);
						}
						break;
					case 7:
						{
						_localctx = new OrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(185);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(186);
						match(OR);
						setState(187);
						expr(5);
						}
						break;
					case 8:
						{
						_localctx = new InExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(188);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(189);
						match(IN);
						setState(190);
						expr(4);
						}
						break;
					case 9:
						{
						_localctx = new AttributeExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(191);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(192);
						match(DOT);
						setState(193);
						match(ID);
						}
						break;
					case 10:
						{
						_localctx = new SubscriptionExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(194);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(195);
						match(LBRACK);
						setState(196);
						expr(0);
						setState(197);
						match(RBRACK);
						}
						break;
					case 11:
						{
						_localctx = new CallExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(199);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(200);
						match(LPAREN);
						setState(209);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & 2132803733L) != 0)) {
							{
							setState(201);
							expr(0);
							setState(206);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(202);
								match(COMMA);
								setState(203);
								expr(0);
								}
								}
								setState(208);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(211);
						match(RPAREN);
						}
						break;
					case 12:
						{
						_localctx = new FilterExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(212);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(213);
						match(PIPE);
						setState(214);
						match(ID);
						setState(227);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
						case 1:
							{
							setState(215);
							match(LPAREN);
							setState(224);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & 2132803733L) != 0)) {
								{
								setState(216);
								expr(0);
								setState(221);
								_errHandler.sync(this);
								_la = _input.LA(1);
								while (_la==COMMA) {
									{
									{
									setState(217);
									match(COMMA);
									setState(218);
									expr(0);
									}
									}
									setState(223);
									_errHandler.sync(this);
									_la = _input.LA(1);
								}
								}
							}

							setState(226);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					case 13:
						{
						_localctx = new IsExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(229);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(230);
						match(IS);
						setState(231);
						match(ID);
						setState(237);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
						case 1:
							{
							setState(232);
							match(LPAREN);
							setState(234);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & 2132803733L) != 0)) {
								{
								setState(233);
								expr(0);
								}
							}

							setState(236);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseLiteralContext extends LiteralContext {
		public TerminalNode FALSE() { return getToken(htmlParser.FALSE, 0); }
		public FalseLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterFalseLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitFalseLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitFalseLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NoneLiteralContext extends LiteralContext {
		public TerminalNode NONE() { return getToken(htmlParser.NONE, 0); }
		public NoneLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterNoneLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitNoneLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitNoneLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DictLiteralContext extends LiteralContext {
		public TerminalNode LBRACE() { return getToken(htmlParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(htmlParser.RBRACE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(htmlParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(htmlParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(htmlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(htmlParser.COMMA, i);
		}
		public DictLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterDictLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitDictLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitDictLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING() { return getToken(htmlParser.STRING, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntLiteralContext extends LiteralContext {
		public TerminalNode INT() { return getToken(htmlParser.INT, 0); }
		public IntLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitIntLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ListLiteralContext extends LiteralContext {
		public TerminalNode LBRACK() { return getToken(htmlParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(htmlParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(htmlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(htmlParser.COMMA, i);
		}
		public ListLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterListLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitListLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitListLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FloatLiteralContext extends LiteralContext {
		public TerminalNode FLOAT() { return getToken(htmlParser.FLOAT, 0); }
		public FloatLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitFloatLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitFloatLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueLiteralContext extends LiteralContext {
		public TerminalNode TRUE() { return getToken(htmlParser.TRUE, 0); }
		public TrueLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterTrueLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitTrueLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitTrueLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_literal);
		int _la;
		try {
			setState(279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				_localctx = new IntLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(244);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(245);
				match(FLOAT);
				}
				break;
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(246);
				match(STRING);
				}
				break;
			case TRUE:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(247);
				match(TRUE);
				}
				break;
			case FALSE:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(248);
				match(FALSE);
				}
				break;
			case NONE:
				_localctx = new NoneLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(249);
				match(NONE);
				}
				break;
			case LBRACE:
				_localctx = new DictLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(250);
				match(LBRACE);
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & 2132803733L) != 0)) {
					{
					setState(251);
					expr(0);
					setState(252);
					match(COLON);
					setState(253);
					expr(0);
					setState(261);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(254);
						match(COMMA);
						setState(255);
						expr(0);
						setState(256);
						match(COLON);
						setState(257);
						expr(0);
						}
						}
						setState(263);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(266);
				match(RBRACE);
				}
				break;
			case LBRACK:
				_localctx = new ListLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(267);
				match(LBRACK);
				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 39)) & ~0x3f) == 0 && ((1L << (_la - 39)) & 2132803733L) != 0)) {
					{
					setState(268);
					expr(0);
					setState(273);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(269);
						match(COMMA);
						setState(270);
						expr(0);
						}
						}
						setState(275);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(278);
				match(RBRACK);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class JinjaSingleTagContext extends ParserRuleContext {
		public TerminalNode TAG_START() { return getToken(htmlParser.TAG_START, 0); }
		public SingleJinjaTagContentContext singleJinjaTagContent() {
			return getRuleContext(SingleJinjaTagContentContext.class,0);
		}
		public TerminalNode TAG_END() { return getToken(htmlParser.TAG_END, 0); }
		public JinjaSingleTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaSingleTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaSingleTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaSingleTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaSingleTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaSingleTagContext jinjaSingleTag() throws RecognitionException {
		JinjaSingleTagContext _localctx = new JinjaSingleTagContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_jinjaSingleTag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(TAG_START);
			setState(282);
			singleJinjaTagContent();
			setState(283);
			match(TAG_END);
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
	public static class SingleJinjaTagContentContext extends ParserRuleContext {
		public TerminalNode SET() { return getToken(htmlParser.SET, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(htmlParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INCLUDE() { return getToken(htmlParser.INCLUDE, 0); }
		public TerminalNode STRING() { return getToken(htmlParser.STRING, 0); }
		public TerminalNode EXTENDS() { return getToken(htmlParser.EXTENDS, 0); }
		public SingleJinjaTagContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleJinjaTagContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterSingleJinjaTagContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitSingleJinjaTagContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitSingleJinjaTagContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleJinjaTagContentContext singleJinjaTagContent() throws RecognitionException {
		SingleJinjaTagContentContext _localctx = new SingleJinjaTagContentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_singleJinjaTagContent);
		try {
			setState(293);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SET:
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				match(SET);
				setState(286);
				match(ID);
				setState(287);
				match(ASSIGN);
				setState(288);
				expr(0);
				}
				break;
			case INCLUDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(289);
				match(INCLUDE);
				setState(290);
				match(STRING);
				}
				break;
			case EXTENDS:
				enterOuterAlt(_localctx, 3);
				{
				setState(291);
				match(EXTENDS);
				setState(292);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class FirstBlockJinjaTagContentContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(htmlParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ELIF() { return getToken(htmlParser.ELIF, 0); }
		public TerminalNode ELSE() { return getToken(htmlParser.ELSE, 0); }
		public TerminalNode FOR() { return getToken(htmlParser.FOR, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public TerminalNode IN() { return getToken(htmlParser.IN, 0); }
		public FirstBlockJinjaTagContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_firstBlockJinjaTagContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterFirstBlockJinjaTagContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitFirstBlockJinjaTagContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitFirstBlockJinjaTagContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FirstBlockJinjaTagContentContext firstBlockJinjaTagContent() throws RecognitionException {
		FirstBlockJinjaTagContentContext _localctx = new FirstBlockJinjaTagContentContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_firstBlockJinjaTagContent);
		try {
			setState(304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(295);
				match(IF);
				setState(296);
				expr(0);
				}
				break;
			case ELIF:
				enterOuterAlt(_localctx, 2);
				{
				setState(297);
				match(ELIF);
				setState(298);
				expr(0);
				}
				break;
			case ELSE:
				enterOuterAlt(_localctx, 3);
				{
				setState(299);
				match(ELSE);
				}
				break;
			case FOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(300);
				match(FOR);
				setState(301);
				match(ID);
				setState(302);
				match(IN);
				setState(303);
				expr(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class EndBlockJinjaTagContentContext extends ParserRuleContext {
		public TerminalNode ENDIF() { return getToken(htmlParser.ENDIF, 0); }
		public TerminalNode ENDFOR() { return getToken(htmlParser.ENDFOR, 0); }
		public EndBlockJinjaTagContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endBlockJinjaTagContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterEndBlockJinjaTagContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitEndBlockJinjaTagContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitEndBlockJinjaTagContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndBlockJinjaTagContentContext endBlockJinjaTagContent() throws RecognitionException {
		EndBlockJinjaTagContentContext _localctx = new EndBlockJinjaTagContentContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_endBlockJinjaTagContent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			_la = _input.LA(1);
			if ( !(_la==ENDIF || _la==ENDFOR) ) {
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
	public static class JinjaBlockContext extends ParserRuleContext {
		public JinjaForBlockContext jinjaForBlock() {
			return getRuleContext(JinjaForBlockContext.class,0);
		}
		public JinjaIfBlockContext jinjaIfBlock() {
			return getRuleContext(JinjaIfBlockContext.class,0);
		}
		public JinjaBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaBlockContext jinjaBlock() throws RecognitionException {
		JinjaBlockContext _localctx = new JinjaBlockContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_jinjaBlock);
		try {
			setState(310);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(308);
				jinjaForBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(309);
				jinjaIfBlock();
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
	public static class JinjaForBlockContext extends ParserRuleContext {
		public List<TerminalNode> TAG_START() { return getTokens(htmlParser.TAG_START); }
		public TerminalNode TAG_START(int i) {
			return getToken(htmlParser.TAG_START, i);
		}
		public TerminalNode FOR() { return getToken(htmlParser.FOR, 0); }
		public TerminalNode ID() { return getToken(htmlParser.ID, 0); }
		public TerminalNode IN() { return getToken(htmlParser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> TAG_END() { return getTokens(htmlParser.TAG_END); }
		public TerminalNode TAG_END(int i) {
			return getToken(htmlParser.TAG_END, i);
		}
		public TerminalNode ENDFOR() { return getToken(htmlParser.ENDFOR, 0); }
		public List<HtmlElementContext> htmlElement() {
			return getRuleContexts(HtmlElementContext.class);
		}
		public HtmlElementContext htmlElement(int i) {
			return getRuleContext(HtmlElementContext.class,i);
		}
		public JinjaForBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaForBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaForBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaForBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaForBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaForBlockContext jinjaForBlock() throws RecognitionException {
		JinjaForBlockContext _localctx = new JinjaForBlockContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_jinjaForBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(TAG_START);
			setState(313);
			match(FOR);
			setState(314);
			match(ID);
			setState(315);
			match(IN);
			setState(316);
			expr(0);
			setState(317);
			match(TAG_END);
			setState(321);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(318);
					htmlElement();
					}
					} 
				}
				setState(323);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			setState(324);
			match(TAG_START);
			setState(325);
			match(ENDFOR);
			setState(326);
			match(TAG_END);
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
	public static class JinjaIfBlockContext extends ParserRuleContext {
		public List<TerminalNode> TAG_START() { return getTokens(htmlParser.TAG_START); }
		public TerminalNode TAG_START(int i) {
			return getToken(htmlParser.TAG_START, i);
		}
		public TerminalNode IF() { return getToken(htmlParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> TAG_END() { return getTokens(htmlParser.TAG_END); }
		public TerminalNode TAG_END(int i) {
			return getToken(htmlParser.TAG_END, i);
		}
		public TerminalNode ENDIF() { return getToken(htmlParser.ENDIF, 0); }
		public List<HtmlElementContext> htmlElement() {
			return getRuleContexts(HtmlElementContext.class);
		}
		public HtmlElementContext htmlElement(int i) {
			return getRuleContext(HtmlElementContext.class,i);
		}
		public List<TerminalNode> ELIF() { return getTokens(htmlParser.ELIF); }
		public TerminalNode ELIF(int i) {
			return getToken(htmlParser.ELIF, i);
		}
		public TerminalNode ELSE() { return getToken(htmlParser.ELSE, 0); }
		public JinjaIfBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaIfBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).enterJinjaIfBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof htmlParserListener ) ((htmlParserListener)listener).exitJinjaIfBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof htmlParserVisitor ) return ((htmlParserVisitor<? extends T>)visitor).visitJinjaIfBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaIfBlockContext jinjaIfBlock() throws RecognitionException {
		JinjaIfBlockContext _localctx = new JinjaIfBlockContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_jinjaIfBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(TAG_START);
			setState(329);
			match(IF);
			setState(330);
			expr(0);
			setState(331);
			match(TAG_END);
			setState(335);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(332);
					htmlElement();
					}
					} 
				}
				setState(337);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(350);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(338);
					match(TAG_START);
					setState(339);
					match(ELIF);
					setState(340);
					expr(0);
					setState(341);
					match(TAG_END);
					setState(345);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(342);
							htmlElement();
							}
							} 
						}
						setState(347);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
					}
					}
					} 
				}
				setState(352);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			setState(362);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(353);
				match(TAG_START);
				setState(354);
				match(ELSE);
				setState(355);
				match(TAG_END);
				setState(359);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(356);
						htmlElement();
						}
						} 
					}
					setState(361);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				}
				}
				break;
			}
			setState(364);
			match(TAG_START);
			setState(365);
			match(ENDIF);
			setState(366);
			match(TAG_END);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		case 4:
			return precpred(_ctx, 6);
		case 5:
			return precpred(_ctx, 5);
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		case 8:
			return precpred(_ctx, 16);
		case 9:
			return precpred(_ctx, 15);
		case 10:
			return precpred(_ctx, 14);
		case 11:
			return precpred(_ctx, 13);
		case 12:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001S\u0171\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0001\u0000\u0003\u0000"+
		",\b\u0000\u0001\u0000\u0005\u0000/\b\u0000\n\u0000\f\u00002\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001<\b\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0005\u0002A\b\u0002\n\u0002\f\u0002D\t\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002J\b\u0002\n\u0002\f\u0002M\t"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002Q\b\u0002\n\u0002\f\u0002T\t"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002Z\b"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002_\b\u0002\n\u0002"+
		"\f\u0002b\t\u0002\u0001\u0002\u0003\u0002e\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003j\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0005\u0005q\b\u0005\n\u0005\f\u0005t\t\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006y\b\u0006\n\u0006\f\u0006"+
		"|\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0005\b\u0088\b\b\n\b"+
		"\f\b\u008b\t\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0003\n"+
		"\u0093\b\n\u0001\u000b\u0001\u000b\u0003\u000b\u0097\b\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u00a6\b\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00cd\b\f\n"+
		"\f\f\f\u00d0\t\f\u0003\f\u00d2\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0005\f\u00dc\b\f\n\f\f\f\u00df\t\f\u0003\f"+
		"\u00e1\b\f\u0001\f\u0003\f\u00e4\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0003\f\u00eb\b\f\u0001\f\u0003\f\u00ee\b\f\u0005\f\u00f0\b\f\n\f\f"+
		"\f\u00f3\t\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u0104"+
		"\b\r\n\r\f\r\u0107\t\r\u0003\r\u0109\b\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0005\r\u0110\b\r\n\r\f\r\u0113\t\r\u0003\r\u0115\b\r\u0001"+
		"\r\u0003\r\u0118\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u0126\b\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u0131\b\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0003\u0012\u0137\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u0140\b\u0013\n"+
		"\u0013\f\u0013\u0143\t\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005"+
		"\u0014\u014e\b\u0014\n\u0014\f\u0014\u0151\t\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0158\b\u0014\n\u0014"+
		"\f\u0014\u015b\t\u0014\u0005\u0014\u015d\b\u0014\n\u0014\f\u0014\u0160"+
		"\t\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0166"+
		"\b\u0014\n\u0014\f\u0014\u0169\t\u0014\u0003\u0014\u016b\b\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0000\u0001\u0018"+
		"\u0015\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(\u0000\u0006\u0001\u0000\f\r\u0001\u0000/1\u0001"+
		"\u0000-.\u0001\u000069\u0001\u000034\u0002\u0000LLQQ\u019e\u0000+\u0001"+
		"\u0000\u0000\u0000\u0002;\u0001\u0000\u0000\u0000\u0004d\u0001\u0000\u0000"+
		"\u0000\u0006f\u0001\u0000\u0000\u0000\bk\u0001\u0000\u0000\u0000\nr\u0001"+
		"\u0000\u0000\u0000\fu\u0001\u0000\u0000\u0000\u000e\u007f\u0001\u0000"+
		"\u0000\u0000\u0010\u0084\u0001\u0000\u0000\u0000\u0012\u008c\u0001\u0000"+
		"\u0000\u0000\u0014\u0092\u0001\u0000\u0000\u0000\u0016\u0094\u0001\u0000"+
		"\u0000\u0000\u0018\u00a5\u0001\u0000\u0000\u0000\u001a\u0117\u0001\u0000"+
		"\u0000\u0000\u001c\u0119\u0001\u0000\u0000\u0000\u001e\u0125\u0001\u0000"+
		"\u0000\u0000 \u0130\u0001\u0000\u0000\u0000\"\u0132\u0001\u0000\u0000"+
		"\u0000$\u0136\u0001\u0000\u0000\u0000&\u0138\u0001\u0000\u0000\u0000("+
		"\u0148\u0001\u0000\u0000\u0000*,\u0005\u0006\u0000\u0000+*\u0001\u0000"+
		"\u0000\u0000+,\u0001\u0000\u0000\u0000,0\u0001\u0000\u0000\u0000-/\u0003"+
		"\u0002\u0001\u0000.-\u0001\u0000\u0000\u0000/2\u0001\u0000\u0000\u0000"+
		"0.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u000013\u0001\u0000\u0000"+
		"\u000020\u0001\u0000\u0000\u000034\u0005\u0000\u0000\u00014\u0001\u0001"+
		"\u0000\u0000\u00005<\u0003\u0004\u0002\u00006<\u0003\b\u0004\u00007<\u0003"+
		"\u0012\t\u00008<\u0003\u0014\n\u00009<\u0003\u0016\u000b\u0000:<\u0005"+
		"\t\u0000\u0000;5\u0001\u0000\u0000\u0000;6\u0001\u0000\u0000\u0000;7\u0001"+
		"\u0000\u0000\u0000;8\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000"+
		";:\u0001\u0000\u0000\u0000<\u0003\u0001\u0000\u0000\u0000=>\u0005\b\u0000"+
		"\u0000>B\u0005\u000f\u0000\u0000?A\u0003\u0006\u0003\u0000@?\u0001\u0000"+
		"\u0000\u0000AD\u0001\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BC\u0001"+
		"\u0000\u0000\u0000CE\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000"+
		"Ee\u0007\u0000\u0000\u0000FG\u0005\b\u0000\u0000GK\u0005\u0010\u0000\u0000"+
		"HJ\u0003\u0006\u0003\u0000IH\u0001\u0000\u0000\u0000JM\u0001\u0000\u0000"+
		"\u0000KI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LN\u0001\u0000"+
		"\u0000\u0000MK\u0001\u0000\u0000\u0000NR\u0005\f\u0000\u0000OQ\u0003\u0002"+
		"\u0001\u0000PO\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001"+
		"\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000SY\u0001\u0000\u0000\u0000"+
		"TR\u0001\u0000\u0000\u0000UV\u0005\b\u0000\u0000VW\u0005\u000e\u0000\u0000"+
		"WX\u0005\u0010\u0000\u0000XZ\u0005\f\u0000\u0000YU\u0001\u0000\u0000\u0000"+
		"YZ\u0001\u0000\u0000\u0000Ze\u0001\u0000\u0000\u0000[\\\u0005\b\u0000"+
		"\u0000\\`\u0005\u0010\u0000\u0000]_\u0003\u0006\u0003\u0000^]\u0001\u0000"+
		"\u0000\u0000_b\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000ac\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000"+
		"ce\u0005\r\u0000\u0000d=\u0001\u0000\u0000\u0000dF\u0001\u0000\u0000\u0000"+
		"d[\u0001\u0000\u0000\u0000e\u0005\u0001\u0000\u0000\u0000fi\u0005\u0011"+
		"\u0000\u0000gh\u0005\u0012\u0000\u0000hj\u0005\u0013\u0000\u0000ig\u0001"+
		"\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000j\u0007\u0001\u0000\u0000"+
		"\u0000kl\u0005\u0007\u0000\u0000lm\u0003\n\u0005\u0000mn\u0005\u0016\u0000"+
		"\u0000n\t\u0001\u0000\u0000\u0000oq\u0003\f\u0006\u0000po\u0001\u0000"+
		"\u0000\u0000qt\u0001\u0000\u0000\u0000rp\u0001\u0000\u0000\u0000rs\u0001"+
		"\u0000\u0000\u0000s\u000b\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000"+
		"\u0000uv\u0005\u001a\u0000\u0000vz\u0005\u0019\u0000\u0000wy\u0003\u000e"+
		"\u0007\u0000xw\u0001\u0000\u0000\u0000y|\u0001\u0000\u0000\u0000zx\u0001"+
		"\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{}\u0001\u0000\u0000\u0000"+
		"|z\u0001\u0000\u0000\u0000}~\u0005 \u0000\u0000~\r\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0005\u001b\u0000\u0000\u0080\u0081\u0005\u001e\u0000\u0000"+
		"\u0081\u0082\u0003\u0010\b\u0000\u0082\u0083\u0005\u001d\u0000\u0000\u0083"+
		"\u000f\u0001\u0000\u0000\u0000\u0084\u0089\u0005\u001c\u0000\u0000\u0085"+
		"\u0086\u0005\u001f\u0000\u0000\u0086\u0088\u0005\u001c\u0000\u0000\u0087"+
		"\u0085\u0001\u0000\u0000\u0000\u0088\u008b\u0001\u0000\u0000\u0000\u0089"+
		"\u0087\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a"+
		"\u0011\u0001\u0000\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008c"+
		"\u008d\u0005\u0003\u0000\u0000\u008d\u008e\u0003\u0018\f\u0000\u008e\u008f"+
		"\u0005\"\u0000\u0000\u008f\u0013\u0001\u0000\u0000\u0000\u0090\u0093\u0003"+
		"\u001c\u000e\u0000\u0091\u0093\u0003$\u0012\u0000\u0092\u0090\u0001\u0000"+
		"\u0000\u0000\u0092\u0091\u0001\u0000\u0000\u0000\u0093\u0015\u0001\u0000"+
		"\u0000\u0000\u0094\u0096\u0005\u0005\u0000\u0000\u0095\u0097\u0005S\u0000"+
		"\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099\u0005R\u0000\u0000"+
		"\u0099\u0017\u0001\u0000\u0000\u0000\u009a\u009b\u0006\f\uffff\uffff\u0000"+
		"\u009b\u00a6\u0003\u001a\r\u0000\u009c\u00a6\u0005B\u0000\u0000\u009d"+
		"\u009e\u0005.\u0000\u0000\u009e\u00a6\u0003\u0018\f\f\u009f\u00a0\u0005"+
		"<\u0000\u0000\u00a0\u00a6\u0003\u0018\f\u000b\u00a1\u00a2\u0005\'\u0000"+
		"\u0000\u00a2\u00a3\u0003\u0018\f\u0000\u00a3\u00a4\u0005(\u0000\u0000"+
		"\u00a4\u00a6\u0001\u0000\u0000\u0000\u00a5\u009a\u0001\u0000\u0000\u0000"+
		"\u00a5\u009c\u0001\u0000\u0000\u0000\u00a5\u009d\u0001\u0000\u0000\u0000"+
		"\u00a5\u009f\u0001\u0000\u0000\u0000\u00a5\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a6\u00f1\u0001\u0000\u0000\u0000\u00a7\u00a8\n\n\u0000\u0000\u00a8"+
		"\u00a9\u00052\u0000\u0000\u00a9\u00f0\u0003\u0018\f\u000b\u00aa\u00ab"+
		"\n\t\u0000\u0000\u00ab\u00ac\u0007\u0001\u0000\u0000\u00ac\u00f0\u0003"+
		"\u0018\f\n\u00ad\u00ae\n\b\u0000\u0000\u00ae\u00af\u0007\u0002\u0000\u0000"+
		"\u00af\u00f0\u0003\u0018\f\t\u00b0\u00b1\n\u0007\u0000\u0000\u00b1\u00b2"+
		"\u0007\u0003\u0000\u0000\u00b2\u00f0\u0003\u0018\f\b\u00b3\u00b4\n\u0006"+
		"\u0000\u0000\u00b4\u00b5\u0007\u0004\u0000\u0000\u00b5\u00f0\u0003\u0018"+
		"\f\u0007\u00b6\u00b7\n\u0005\u0000\u0000\u00b7\u00b8\u0005:\u0000\u0000"+
		"\u00b8\u00f0\u0003\u0018\f\u0006\u00b9\u00ba\n\u0004\u0000\u0000\u00ba"+
		"\u00bb\u0005;\u0000\u0000\u00bb\u00f0\u0003\u0018\f\u0005\u00bc\u00bd"+
		"\n\u0003\u0000\u0000\u00bd\u00be\u0005=\u0000\u0000\u00be\u00f0\u0003"+
		"\u0018\f\u0004\u00bf\u00c0\n\u0010\u0000\u0000\u00c0\u00c1\u0005#\u0000"+
		"\u0000\u00c1\u00f0\u0005B\u0000\u0000\u00c2\u00c3\n\u000f\u0000\u0000"+
		"\u00c3\u00c4\u0005)\u0000\u0000\u00c4\u00c5\u0003\u0018\f\u0000\u00c5"+
		"\u00c6\u0005*\u0000\u0000\u00c6\u00f0\u0001\u0000\u0000\u0000\u00c7\u00c8"+
		"\n\u000e\u0000\u0000\u00c8\u00d1\u0005\'\u0000\u0000\u00c9\u00ce\u0003"+
		"\u0018\f\u0000\u00ca\u00cb\u0005$\u0000\u0000\u00cb\u00cd\u0003\u0018"+
		"\f\u0000\u00cc\u00ca\u0001\u0000\u0000\u0000\u00cd\u00d0\u0001\u0000\u0000"+
		"\u0000\u00ce\u00cc\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d2\u0001\u0000\u0000\u0000\u00d0\u00ce\u0001\u0000\u0000"+
		"\u0000\u00d1\u00c9\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00f0\u0005(\u0000\u0000"+
		"\u00d4\u00d5\n\r\u0000\u0000\u00d5\u00d6\u0005&\u0000\u0000\u00d6\u00e3"+
		"\u0005B\u0000\u0000\u00d7\u00e0\u0005\'\u0000\u0000\u00d8\u00dd\u0003"+
		"\u0018\f\u0000\u00d9\u00da\u0005$\u0000\u0000\u00da\u00dc\u0003\u0018"+
		"\f\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00dc\u00df\u0001\u0000\u0000"+
		"\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000"+
		"\u0000\u00de\u00e1\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000\u0000"+
		"\u0000\u00e0\u00d8\u0001\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000"+
		"\u0000\u00e1\u00e2\u0001\u0000\u0000\u0000\u00e2\u00e4\u0005(\u0000\u0000"+
		"\u00e3\u00d7\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000"+
		"\u00e4\u00f0\u0001\u0000\u0000\u0000\u00e5\u00e6\n\u0002\u0000\u0000\u00e6"+
		"\u00e7\u0005>\u0000\u0000\u00e7\u00ed\u0005B\u0000\u0000\u00e8\u00ea\u0005"+
		"\'\u0000\u0000\u00e9\u00eb\u0003\u0018\f\u0000\u00ea\u00e9\u0001\u0000"+
		"\u0000\u0000\u00ea\u00eb\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000"+
		"\u0000\u0000\u00ec\u00ee\u0005(\u0000\u0000\u00ed\u00e8\u0001\u0000\u0000"+
		"\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee\u00f0\u0001\u0000\u0000"+
		"\u0000\u00ef\u00a7\u0001\u0000\u0000\u0000\u00ef\u00aa\u0001\u0000\u0000"+
		"\u0000\u00ef\u00ad\u0001\u0000\u0000\u0000\u00ef\u00b0\u0001\u0000\u0000"+
		"\u0000\u00ef\u00b3\u0001\u0000\u0000\u0000\u00ef\u00b6\u0001\u0000\u0000"+
		"\u0000\u00ef\u00b9\u0001\u0000\u0000\u0000\u00ef\u00bc\u0001\u0000\u0000"+
		"\u0000\u00ef\u00bf\u0001\u0000\u0000\u0000\u00ef\u00c2\u0001\u0000\u0000"+
		"\u0000\u00ef\u00c7\u0001\u0000\u0000\u0000\u00ef\u00d4\u0001\u0000\u0000"+
		"\u0000\u00ef\u00e5\u0001\u0000\u0000\u0000\u00f0\u00f3\u0001\u0000\u0000"+
		"\u0000\u00f1\u00ef\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000"+
		"\u0000\u00f2\u0019\u0001\u0000\u0000\u0000\u00f3\u00f1\u0001\u0000\u0000"+
		"\u0000\u00f4\u0118\u0005C\u0000\u0000\u00f5\u0118\u0005D\u0000\u0000\u00f6"+
		"\u0118\u0005E\u0000\u0000\u00f7\u0118\u0005@\u0000\u0000\u00f8\u0118\u0005"+
		"A\u0000\u0000\u00f9\u0118\u0005?\u0000\u0000\u00fa\u0108\u0005+\u0000"+
		"\u0000\u00fb\u00fc\u0003\u0018\f\u0000\u00fc\u00fd\u0005%\u0000\u0000"+
		"\u00fd\u0105\u0003\u0018\f\u0000\u00fe\u00ff\u0005$\u0000\u0000\u00ff"+
		"\u0100\u0003\u0018\f\u0000\u0100\u0101\u0005%\u0000\u0000\u0101\u0102"+
		"\u0003\u0018\f\u0000\u0102\u0104\u0001\u0000\u0000\u0000\u0103\u00fe\u0001"+
		"\u0000\u0000\u0000\u0104\u0107\u0001\u0000\u0000\u0000\u0105\u0103\u0001"+
		"\u0000\u0000\u0000\u0105\u0106\u0001\u0000\u0000\u0000\u0106\u0109\u0001"+
		"\u0000\u0000\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0108\u00fb\u0001"+
		"\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000\u0109\u010a\u0001"+
		"\u0000\u0000\u0000\u010a\u0118\u0005,\u0000\u0000\u010b\u0114\u0005)\u0000"+
		"\u0000\u010c\u0111\u0003\u0018\f\u0000\u010d\u010e\u0005$\u0000\u0000"+
		"\u010e\u0110\u0003\u0018\f\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u0110"+
		"\u0113\u0001\u0000\u0000\u0000\u0111\u010f\u0001\u0000\u0000\u0000\u0111"+
		"\u0112\u0001\u0000\u0000\u0000\u0112\u0115\u0001\u0000\u0000\u0000\u0113"+
		"\u0111\u0001\u0000\u0000\u0000\u0114\u010c\u0001\u0000\u0000\u0000\u0114"+
		"\u0115\u0001\u0000\u0000\u0000\u0115\u0116\u0001\u0000\u0000\u0000\u0116"+
		"\u0118\u0005*\u0000\u0000\u0117\u00f4\u0001\u0000\u0000\u0000\u0117\u00f5"+
		"\u0001\u0000\u0000\u0000\u0117\u00f6\u0001\u0000\u0000\u0000\u0117\u00f7"+
		"\u0001\u0000\u0000\u0000\u0117\u00f8\u0001\u0000\u0000\u0000\u0117\u00f9"+
		"\u0001\u0000\u0000\u0000\u0117\u00fa\u0001\u0000\u0000\u0000\u0117\u010b"+
		"\u0001\u0000\u0000\u0000\u0118\u001b\u0001\u0000\u0000\u0000\u0119\u011a"+
		"\u0005\u0004\u0000\u0000\u011a\u011b\u0003\u001e\u000f\u0000\u011b\u011c"+
		"\u0005H\u0000\u0000\u011c\u001d\u0001\u0000\u0000\u0000\u011d\u011e\u0005"+
		"M\u0000\u0000\u011e\u011f\u0005B\u0000\u0000\u011f\u0120\u00055\u0000"+
		"\u0000\u0120\u0126\u0003\u0018\f\u0000\u0121\u0122\u0005N\u0000\u0000"+
		"\u0122\u0126\u0005E\u0000\u0000\u0123\u0124\u0005O\u0000\u0000\u0124\u0126"+
		"\u0005E\u0000\u0000\u0125\u011d\u0001\u0000\u0000\u0000\u0125\u0121\u0001"+
		"\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0126\u001f\u0001"+
		"\u0000\u0000\u0000\u0127\u0128\u0005I\u0000\u0000\u0128\u0131\u0003\u0018"+
		"\f\u0000\u0129\u012a\u0005J\u0000\u0000\u012a\u0131\u0003\u0018\f\u0000"+
		"\u012b\u0131\u0005K\u0000\u0000\u012c\u012d\u0005P\u0000\u0000\u012d\u012e"+
		"\u0005B\u0000\u0000\u012e\u012f\u0005=\u0000\u0000\u012f\u0131\u0003\u0018"+
		"\f\u0000\u0130\u0127\u0001\u0000\u0000\u0000\u0130\u0129\u0001\u0000\u0000"+
		"\u0000\u0130\u012b\u0001\u0000\u0000\u0000\u0130\u012c\u0001\u0000\u0000"+
		"\u0000\u0131!\u0001\u0000\u0000\u0000\u0132\u0133\u0007\u0005\u0000\u0000"+
		"\u0133#\u0001\u0000\u0000\u0000\u0134\u0137\u0003&\u0013\u0000\u0135\u0137"+
		"\u0003(\u0014\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0135\u0001"+
		"\u0000\u0000\u0000\u0137%\u0001\u0000\u0000\u0000\u0138\u0139\u0005\u0004"+
		"\u0000\u0000\u0139\u013a\u0005P\u0000\u0000\u013a\u013b\u0005B\u0000\u0000"+
		"\u013b\u013c\u0005=\u0000\u0000\u013c\u013d\u0003\u0018\f\u0000\u013d"+
		"\u0141\u0005H\u0000\u0000\u013e\u0140\u0003\u0002\u0001\u0000\u013f\u013e"+
		"\u0001\u0000\u0000\u0000\u0140\u0143\u0001\u0000\u0000\u0000\u0141\u013f"+
		"\u0001\u0000\u0000\u0000\u0141\u0142\u0001\u0000\u0000\u0000\u0142\u0144"+
		"\u0001\u0000\u0000\u0000\u0143\u0141\u0001\u0000\u0000\u0000\u0144\u0145"+
		"\u0005\u0004\u0000\u0000\u0145\u0146\u0005Q\u0000\u0000\u0146\u0147\u0005"+
		"H\u0000\u0000\u0147\'\u0001\u0000\u0000\u0000\u0148\u0149\u0005\u0004"+
		"\u0000\u0000\u0149\u014a\u0005I\u0000\u0000\u014a\u014b\u0003\u0018\f"+
		"\u0000\u014b\u014f\u0005H\u0000\u0000\u014c\u014e\u0003\u0002\u0001\u0000"+
		"\u014d\u014c\u0001\u0000\u0000\u0000\u014e\u0151\u0001\u0000\u0000\u0000"+
		"\u014f\u014d\u0001\u0000\u0000\u0000\u014f\u0150\u0001\u0000\u0000\u0000"+
		"\u0150\u015e\u0001\u0000\u0000\u0000\u0151\u014f\u0001\u0000\u0000\u0000"+
		"\u0152\u0153\u0005\u0004\u0000\u0000\u0153\u0154\u0005J\u0000\u0000\u0154"+
		"\u0155\u0003\u0018\f\u0000\u0155\u0159\u0005H\u0000\u0000\u0156\u0158"+
		"\u0003\u0002\u0001\u0000\u0157\u0156\u0001\u0000\u0000\u0000\u0158\u015b"+
		"\u0001\u0000\u0000\u0000\u0159\u0157\u0001\u0000\u0000\u0000\u0159\u015a"+
		"\u0001\u0000\u0000\u0000\u015a\u015d\u0001\u0000\u0000\u0000\u015b\u0159"+
		"\u0001\u0000\u0000\u0000\u015c\u0152\u0001\u0000\u0000\u0000\u015d\u0160"+
		"\u0001\u0000\u0000\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015e\u015f"+
		"\u0001\u0000\u0000\u0000\u015f\u016a\u0001\u0000\u0000\u0000\u0160\u015e"+
		"\u0001\u0000\u0000\u0000\u0161\u0162\u0005\u0004\u0000\u0000\u0162\u0163"+
		"\u0005K\u0000\u0000\u0163\u0167\u0005H\u0000\u0000\u0164\u0166\u0003\u0002"+
		"\u0001\u0000\u0165\u0164\u0001\u0000\u0000\u0000\u0166\u0169\u0001\u0000"+
		"\u0000\u0000\u0167\u0165\u0001\u0000\u0000\u0000\u0167\u0168\u0001\u0000"+
		"\u0000\u0000\u0168\u016b\u0001\u0000\u0000\u0000\u0169\u0167\u0001\u0000"+
		"\u0000\u0000\u016a\u0161\u0001\u0000\u0000\u0000\u016a\u016b\u0001\u0000"+
		"\u0000\u0000\u016b\u016c\u0001\u0000\u0000\u0000\u016c\u016d\u0005\u0004"+
		"\u0000\u0000\u016d\u016e\u0005L\u0000\u0000\u016e\u016f\u0005H\u0000\u0000"+
		"\u016f)\u0001\u0000\u0000\u0000\'+0;BKRY`dirz\u0089\u0092\u0096\u00a5"+
		"\u00ce\u00d1\u00dd\u00e0\u00e3\u00ea\u00ed\u00ef\u00f1\u0105\u0108\u0111"+
		"\u0114\u0117\u0125\u0130\u0136\u0141\u014f\u0159\u015e\u0167\u016a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}