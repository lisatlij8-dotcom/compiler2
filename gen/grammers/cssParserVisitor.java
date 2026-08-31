// Generated from grammers/cssParser.g4 by ANTLR 4.13.2
package grammers;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link cssParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface cssParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link cssParser#stylesheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylesheet(cssParser.StylesheetContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#mediaRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMediaRule(cssParser.MediaRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#ruleset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleset(cssParser.RulesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(cssParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#complexSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexSelector(cssParser.ComplexSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#simpleSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSelector(cssParser.SimpleSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperties(cssParser.PropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(cssParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#valueFragment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueFragment(cssParser.ValueFragmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link cssParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(cssParser.FunctionCallContext ctx);
}