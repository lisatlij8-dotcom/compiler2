package visitor;

import AST_H_C.*;
import SymbolTable.WebSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSymbolTableVisitor {

    public static List<WebSymbol> webSymbols = new ArrayList<>();
    private String currentFile;
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final Set<String> JINJA_KEYWORDS = Set.of(
            "and", "or", "not", "in", "is", "true", "false", "none",
            "True", "False", "None"
    );

    public WebSymbolTableVisitor(String fileName) {
        this.currentFile = fileName;
    }

    public static void clearReport() {
        webSymbols.clear();
    }

    public static List<WebSymbol> getSymbols() {
        return Collections.unmodifiableList(webSymbols);
    }

    public void build(Node node) {
        if (node == null) return;


        if (node instanceof HtmlTag) {
            HtmlTag tag = (HtmlTag) node;
            addSymbol(tag.getTagName(), "HTML_TAG", tag.getNumberOfLine());

            if (tag.getAttributes() != null) {
                for (HtmlAttribute attr : tag.getAttributes()) {
                    String attrName = attr.getAttributeName();
                    String attrValue = attr.getAttributeValue();

                    if (attrName.equalsIgnoreCase("id")) {
                        addSymbol(attrValue, "HTML_ID", attr.getNumberOfLine());
                    } else if (attrName.equalsIgnoreCase("class")) {

                        for (String cls : attrValue.split("\\s+")) {
                            if (!cls.isEmpty()) {
                                addSymbol(cls, "HTML_CLASS", attr.getNumberOfLine());
                            }
                        }
                    } else if (attrName.equalsIgnoreCase("href")) {
                        addSymbol(attrValue, "LINK_ROUTE", attr.getNumberOfLine());
                    } else if (tag.getTagName().equalsIgnoreCase("form") && attrName.equalsIgnoreCase("action")) {
                        addSymbol(attrValue, "FORM_ACTION", attr.getNumberOfLine());
                    } else if (isFormFieldTag(tag.getTagName()) && attrName.equalsIgnoreCase("name")) {
                        addSymbol(attrValue, "FORM_FIELD", attr.getNumberOfLine());
                    }
                }
            }
            if (tag.getChildren() != null) {
                for (HtmlElement child : tag.getChildren()) build(child);
            }
        }
        else if (node instanceof CSS_Style) {
            CSS_Style style = (CSS_Style) node;
            if (style.getRuleSets() != null) {
                for (CSSRuleSet ruleSet : style.getRuleSets()) {
                    build(ruleSet);
                }
            }
        }
        else if (node instanceof CSSRuleSet) {
            CSSRuleSet rule = (CSSRuleSet) node;
            addSymbol(rule.getSelector(), "CSS_SELECTOR", rule.getNumberOfLine());
        }
        else if (node instanceof JinjaExpression) {
            JinjaExpression jinja = (JinjaExpression) node;
            addJinjaVariableUses(jinja.getExpression(), jinja.getNumberOfLine());
        }
        else if (node instanceof JinjaForBlock) {
            JinjaForBlock forBlock = (JinjaForBlock) node;
            addSymbol(forBlock.getLoopVariable(), "JINJA_LOOP_VARIABLE", forBlock.getNumberOfLine());
            addJinjaVariableUses(forBlock.getIterableExpression(), forBlock.getNumberOfLine());
            if (forBlock.getBody() != null) {
                for (HtmlElement element : forBlock.getBody()) build(element);
            }
        }
        else if (node instanceof JinjaIfBlock) {
            JinjaIfBlock ifBlock = (JinjaIfBlock) node;
            addJinjaVariableUses(ifBlock.getCondition(), ifBlock.getNumberOfLine());
            if (ifBlock.getIfBody() != null) {
                for (HtmlElement element : ifBlock.getIfBody()) {
                    build(element);
                }
            }
            if (ifBlock.getElifBranches() != null) {
                for (JinjaIfBlock.ElifBranch branch : ifBlock.getElifBranches()) {
                    addJinjaVariableUses(branch.getCondition(), ifBlock.getNumberOfLine());
                    for (HtmlElement element : branch.getBody()) {
                        build(element);
                    }
                }
            }
            if (ifBlock.getElseBody() != null) {
                for (HtmlElement element : ifBlock.getElseBody()) {
                    build(element);
                }
            }
        }
        else if (node instanceof JinjaSingleTag) {
            JinjaSingleTag tag = (JinjaSingleTag) node;
            if ("set".equalsIgnoreCase(tag.getTagType())) {
                addSymbol(tag.getTarget(), "JINJA_SET_VARIABLE", tag.getNumberOfLine());
                addJinjaVariableUses(tag.getValue(), tag.getNumberOfLine());
            } else if ("include".equalsIgnoreCase(tag.getTagType())) {
                addSymbol(unquote(tag.getValue()), "JINJA_INCLUDE", tag.getNumberOfLine());
            } else if ("extends".equalsIgnoreCase(tag.getTagType())) {
                addSymbol(unquote(tag.getValue()), "JINJA_EXTENDS", tag.getNumberOfLine());
            }
        }

    }

    private void addSymbol(String name, String type, int line) {
        if (name != null && !name.isBlank()) {
            webSymbols.add(new WebSymbol(name.trim(), type, line, currentFile));
        }
    }

    private boolean isFormFieldTag(String tagName) {
        return tagName.equalsIgnoreCase("input")
                || tagName.equalsIgnoreCase("select")
                || tagName.equalsIgnoreCase("textarea")
                || tagName.equalsIgnoreCase("button");
    }

    private void addJinjaVariableUses(String expression, int line) {
        if (expression == null || expression.isBlank()) {
            return;
        }

        Set<String> names = new LinkedHashSet<>();
        Matcher matcher = IDENTIFIER_PATTERN.matcher(expression);
        while (matcher.find()) {
            String name = matcher.group();
            int start = matcher.start();
            char previous = start > 0 ? expression.charAt(start - 1) : '\0';
            if (previous == '.' || previous == '|' || JINJA_KEYWORDS.contains(name)) {
                continue;
            }
            names.add(name);
        }

        for (String name : names) {
            addSymbol(name, "JINJA_VARIABLE", line);
        }
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

    public static void printReport() {

        System.out.format("%-25s | %-15s | %-6s | %-20s\n", "Symbol Name", "Type", "Line", "Source File");
        System.out.println("-".repeat(80));
        for (WebSymbol s : webSymbols) {
            System.out.println(s);
        }

    }
}
