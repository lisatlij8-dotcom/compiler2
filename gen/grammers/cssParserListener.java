// Generated from grammers/cssParser.g4 by ANTLR 4.13.2
package grammers;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link cssParser}.
 */
public interface cssParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link cssParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(cssParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(cssParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#mediaRule}.
	 * @param ctx the parse tree
	 */
	void enterMediaRule(cssParser.MediaRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#mediaRule}.
	 * @param ctx the parse tree
	 */
	void exitMediaRule(cssParser.MediaRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void enterRuleset(cssParser.RulesetContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void exitRuleset(cssParser.RulesetContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(cssParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(cssParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#complexSelector}.
	 * @param ctx the parse tree
	 */
	void enterComplexSelector(cssParser.ComplexSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#complexSelector}.
	 * @param ctx the parse tree
	 */
	void exitComplexSelector(cssParser.ComplexSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#simpleSelector}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelector(cssParser.SimpleSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#simpleSelector}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelector(cssParser.SimpleSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#properties}.
	 * @param ctx the parse tree
	 */
	void enterProperties(cssParser.PropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#properties}.
	 * @param ctx the parse tree
	 */
	void exitProperties(cssParser.PropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(cssParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(cssParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#valueFragment}.
	 * @param ctx the parse tree
	 */
	void enterValueFragment(cssParser.ValueFragmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#valueFragment}.
	 * @param ctx the parse tree
	 */
	void exitValueFragment(cssParser.ValueFragmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link cssParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(cssParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link cssParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(cssParser.FunctionCallContext ctx);
}