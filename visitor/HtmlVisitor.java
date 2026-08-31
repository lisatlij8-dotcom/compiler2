package visitor;

import AST_H_C.*;
import grammers.htmlParser;
import grammers.htmlParserBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class HtmlVisitor extends htmlParserBaseVisitor<Node> {

    private int getLineNumber(ParseTree ctx) {
        if (ctx instanceof org.antlr.v4.runtime.ParserRuleContext) {
            Token start = ((org.antlr.v4.runtime.ParserRuleContext) ctx).getStart();
            if (start != null) {
                return start.getLine();
            }
        }
        return -1;
    }

    private String unquote(String value) {
        if (value != null && value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }


    @Override
    public Node visitHtmlDocument(htmlParser.HtmlDocumentContext ctx) {
        int line = getLineNumber(ctx);
        List<HtmlElement> children = new ArrayList<>();

        for (htmlParser.HtmlElementContext elemCtx : ctx.htmlElement()) {
            Node node = visit(elemCtx);
            if (node instanceof HtmlElement) {
                children.add((HtmlElement) node);
            }
        }

        return new HtmlTag("HTML_DOCUMENT", line, "html", new ArrayList<>(), children, null);
    }


    @Override
    public Node visitTag_html(htmlParser.Tag_htmlContext ctx) {
        return visitHtmlTag(ctx.htmlTag());
    }

    @Override
    public Node visitCssSty(htmlParser.CssStyContext ctx) {
        return visitCssStyle(ctx.cssStyle());
    }

    @Override
    public Node visitJinjaExpr(htmlParser.JinjaExprContext ctx) {
        return visitJinjaExpression(ctx.jinjaExpression());
    }

    @Override
    public Node visitJinja_Tag(htmlParser.Jinja_TagContext ctx) {
        return visitJinjaTag(ctx.jinjaTag());
    }

    @Override
    public Node visitJinjaComm(htmlParser.JinjaCommContext ctx) {
        return visitJinjaComment(ctx.jinjaComment());
    }

    @Override
    public Node visitText(htmlParser.TextContext ctx) {
        int line = getLineNumber(ctx);
        String text = ctx.HTML_TEXT().getText();
        return new HtmlText("HTML_TEXT", line, text);
    }


    public Node visitHtmlTag(htmlParser.HtmlTagContext ctx) {
        int line = getLineNumber(ctx);

        String tagName = "";
        if (ctx.VOID_TAG_NAME() != null) {
            tagName = ctx.VOID_TAG_NAME().getText();
        } else if (ctx.TAG_NAME() != null && ctx.TAG_NAME().size() > 0) {
            tagName = ctx.TAG_NAME(0).getText();
        }

        // Void elements (meta, link, input, br, ...) never have a closing-tag
        // production at all - see htmlTag's VOID_TAG_NAME alternative - so
        // there is no closingTagName to extract for them, by construction.
        String closingTagName = null;
        if (ctx.VOID_TAG_NAME() == null && ctx.TAG_NAME() != null && ctx.TAG_NAME().size() > 1) {
            closingTagName = ctx.TAG_NAME(1).getText();
        }

        // 2. جمع السمات كـ List<HtmlAttribute> (مهم!)
        List<HtmlAttribute> attributes = new ArrayList<>();
        if (ctx.htmlAttribute() != null) {
            for (htmlParser.HtmlAttributeContext attrCtx : ctx.htmlAttribute()) {
                String attrName = attrCtx.ATTR_NAME().getText().trim();
                String attrValue = "";
                if (attrCtx.ATTRIBUTE_VALUE() != null) {
                    attrValue = unquote(attrCtx.ATTRIBUTE_VALUE().getText());
                }
                attributes.add(new HtmlAttribute("HTML_ATTRIBUTE", line, attrName, attrValue));
            }
        }
        List<HtmlElement> children = new ArrayList<>();
        if (ctx.htmlElement() != null) {
            for (htmlParser.HtmlElementContext childCtx : ctx.htmlElement()) {
                Node childNode = visit(childCtx);
                if (childNode instanceof HtmlElement) {
                    children.add((HtmlElement) childNode);
                }
            }
        }

        return new HtmlTag("HTML_TAG", line, tagName, attributes, children, closingTagName);
    }


    public Node visitCssStyle(htmlParser.CssStyleContext ctx) {
        int line = getLineNumber(ctx);
        List<CSSRuleSet> ruleSets = new ArrayList<>();

        if (ctx.stylesheet() != null && ctx.stylesheet().ruleset() != null) {
            for (htmlParser.RulesetContext ruleCtx : ctx.stylesheet().ruleset()) {
                CSSRuleSet ruleSet = (CSSRuleSet) visitRuleset(ruleCtx);
                if (ruleSet != null) {
                    ruleSets.add(ruleSet);
                }
            }
        }

        return new CSS_Style("CSS_STYLE", line, ruleSets);
    }

    @Override
    public Node visitRuleset(htmlParser.RulesetContext ctx) {
        int line = getLineNumber(ctx);
        String selector = ctx.CSS_SELECTOR().getText();

        List<CssProperty> properties = new ArrayList<>();
        if (ctx.properties() != null) {
            for (htmlParser.PropertiesContext propCtx : ctx.properties()) {
                CssProperty property = (CssProperty) visitProperties(propCtx);
                if (property != null) {
                    properties.add(property);
                }
            }
        }

        return new CSSRuleSet("CSS_RULESET", line, selector, properties);
    }

    @Override
    public Node visitProperties(htmlParser.PropertiesContext ctx) {
        int line = getLineNumber(ctx);
        String propertyName = ctx.PROPERTY().getText();

        StringBuilder valueBuilder = new StringBuilder();
        if (ctx.value() != null && ctx.value().VALUE() != null) {
            List<org.antlr.v4.runtime.tree.TerminalNode> values = ctx.value().VALUE();
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) {
                    valueBuilder.append(", ");
                }
                valueBuilder.append(values.get(i).getText());
            }
        }

        return new CssProperty("CSS_PROPERTY", line, propertyName, valueBuilder.toString());
    }

    public Node visitJinjaExpression(htmlParser.JinjaExpressionContext ctx) {
        int line = getLineNumber(ctx);
        String expression = ctx.expr().getText();
        return new JinjaExpression("JINJA_EXPRESSION", line, expression);
    }

    public Node visitJinjaTag(htmlParser.JinjaTagContext ctx) {
        if (ctx.jinjaSingleTag() != null) {
            return visitJinjaSingleTag(ctx.jinjaSingleTag());
        } else if (ctx.jinjaBlock() != null) {
            return visitJinjaBlock(ctx.jinjaBlock());
        }
        return null;
    }

    @Override
    public Node visitJinjaSingleTag(htmlParser.JinjaSingleTagContext ctx) {
        int line = getLineNumber(ctx);
        htmlParser.SingleJinjaTagContentContext contentCtx = ctx.singleJinjaTagContent();
        String tagContent = contentCtx.getText();
        String tagType = "";

        if (contentCtx.SET() != null) {
            tagType = "set";
            tagContent = "set " + contentCtx.ID().getText() + " = " + contentCtx.expr().getText();
            return new JinjaSingleTag(
                    "JINJA_SINGLE_TAG",
                    line,
                    tagType,
                    tagContent,
                    contentCtx.ID().getText(),
                    contentCtx.expr().getText()
            );
        } else if (contentCtx.INCLUDE() != null) {
            tagType = "include";
            tagContent = "include " + contentCtx.STRING().getText();
            return new JinjaSingleTag(
                    "JINJA_SINGLE_TAG",
                    line,
                    tagType,
                    tagContent,
                    null,
                    contentCtx.STRING().getText()
            );
        } else if (contentCtx.EXTENDS() != null) {
            tagType = "extends";
            tagContent = "extends " + contentCtx.STRING().getText();
            return new JinjaSingleTag(
                    "JINJA_SINGLE_TAG",
                    line,
                    tagType,
                    tagContent,
                    null,
                    contentCtx.STRING().getText()
            );
        }

        return new JinjaSingleTag("JINJA_SINGLE_TAG", line, tagType, tagContent);


    }

    @Override
    public Node visitJinjaBlock(htmlParser.JinjaBlockContext ctx) {
        if (ctx.jinjaForBlock() != null) {
            return visitJinjaForBlock(ctx.jinjaForBlock());
        } else if (ctx.jinjaIfBlock() != null) {
            return visitJinjaIfBlock(ctx.jinjaIfBlock());
        }
        return null;
    }

    @Override
    public Node visitJinjaForBlock(htmlParser.JinjaForBlockContext ctx) {
        int line = getLineNumber(ctx);
        String variable = ctx.ID().getText();
        String collection = ctx.expr().getText();

        List<HtmlElement> body = new ArrayList<>();
        for (htmlParser.HtmlElementContext elemCtx : ctx.htmlElement()) {
            Node node = visit(elemCtx);
            if (node instanceof HtmlElement) {
                body.add((HtmlElement) node);
            }
        }

        return new JinjaForBlock("JINJA_FOR_BLOCK", line, variable, collection, body);
    }

    @Override
    public Node visitJinjaIfBlock(htmlParser.JinjaIfBlockContext ctx) {
        int line = getLineNumber(ctx);

        String condition = ctx.expr(0).getText();

        List<HtmlElement> ifBody = new ArrayList<>();
        List<JinjaIfBlock.ElifBranch> elifBranches = new ArrayList<>();
        List<HtmlElement> elseBody = new ArrayList<>();
        List<HtmlElement> currentBody = ifBody;
        boolean readingElifCondition = false;

        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNode) {
                int type = ((TerminalNode) child).getSymbol().getType();
                if (type == htmlParser.ELIF) {
                    readingElifCondition = true;
                } else if (type == htmlParser.ELSE) {
                    currentBody = elseBody;
                    readingElifCondition = false;
                }
            } else if (child instanceof htmlParser.ExprContext && readingElifCondition) {
                currentBody = new ArrayList<>();
                elifBranches.add(new JinjaIfBlock.ElifBranch(child.getText(), currentBody));
                readingElifCondition = false;
            } else if (child instanceof htmlParser.HtmlElementContext) {
                Node node = visit(child);
                if (node instanceof HtmlElement) {
                    currentBody.add((HtmlElement) node);
                }
            }
        }

        return new JinjaIfBlock("JINJA_IF_BLOCK", line, condition, ifBody, elifBranches, elseBody);
    }


    public Node visitJinjaComment(htmlParser.JinjaCommentContext ctx) {
        int line = getLineNumber(ctx);
        String commentText = ctx.COMMENT_TEXT() != null ? ctx.COMMENT_TEXT().getText() : "";
        return new HtmlText("JINJA_COMMENT", line, "{{# " + commentText + " #}}");
    }
}
