package visitor;

import AST_H_C.CSSRuleSet;
import AST_H_C.CSS_Style;
import AST_H_C.CssProperty;
import grammers.cssParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a standalone CSS parse tree (cssParser.g4, entry rule "stylesheet")
 * into the same CSS_Style/CSSRuleSet/CssProperty AST node types already used
 * for CSS embedded in HTML <style> blocks, so both flows share one AST shape
 * and one downstream toolchain (symbol table, semantic analysis, printing).
 */
public class CssVisitor {

    public CSS_Style visitStylesheet(cssParser.StylesheetContext ctx) {
        List<CSSRuleSet> ruleSets = new ArrayList<>();
        for (cssParser.RulesetContext ruleCtx : ctx.ruleset()) {
            ruleSets.add(buildRuleSet(ruleCtx));
        }
        // Minimal @media representation: rules nested inside a media block
        // are flattened into the same flat list as top-level rules, so
        // WebSymbolTableVisitor / semantic selector validation keep working
        // unchanged for them without needing a dedicated "media block" AST
        // node type.
        for (cssParser.MediaRuleContext mediaCtx : ctx.mediaRule()) {
            for (cssParser.RulesetContext ruleCtx : mediaCtx.ruleset()) {
                ruleSets.add(buildRuleSet(ruleCtx));
            }
        }
        return new CSS_Style("CSS_STYLE", line(ctx), ruleSets);
    }

    private CSSRuleSet buildRuleSet(cssParser.RulesetContext ctx) {
        String selector = ctx.selector() != null ? rawText(ctx.selector()).trim() : "";
        List<CssProperty> properties = new ArrayList<>();
        for (cssParser.PropertiesContext propCtx : ctx.properties()) {
            properties.add(buildProperty(propCtx));
        }
        return new CSSRuleSet("CSS_RULESET", line(ctx), selector, properties);
    }

    private CssProperty buildProperty(cssParser.PropertiesContext ctx) {
        String propertyName = ctx.IDENT() != null ? ctx.IDENT().getText() : "";
        String value = ctx.value() != null ? rawText(ctx.value()).trim() : "";
        return new CssProperty("CSS_PROPERTY", line(ctx), propertyName, value);
    }

    /**
     * Reconstructs the exact original source text for a rule context,
     * reading directly from the character stream instead of concatenating
     * token text (which drops whitespace). This lets the grammar stay
     * whitespace-insensitive for selectors/values (no need to distinguish
     * a compound run from a descendant combinator, or track spacing between
     * value fragments at the grammar level) while still preserving grouped
     * selectors ("h1, h2, h3"), compound selectors (".a.b"), descendant
     * selectors (".a b"), and multi-token values ("1px solid #fff") exactly
     * as written.
     */
    private String rawText(ParserRuleContext ctx) {
        if (ctx == null) {
            return "";
        }
        if (ctx.getStart() == null || ctx.getStop() == null
                || ctx.getStop().getStopIndex() < ctx.getStart().getStartIndex()) {
            return ctx.getText();
        }
        Interval interval = Interval.of(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        return ctx.getStart().getInputStream().getText(interval);
    }

    private int line(ParserRuleContext ctx) {
        return ctx.getStart() != null ? ctx.getStart().getLine() : -1;
    }
}
