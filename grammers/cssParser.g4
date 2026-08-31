parser grammar cssParser;

options {
    tokenVocab = cssLexer;
}

stylesheet: (ruleset | mediaRule)* EOF ;

// Minimal @media support: the condition itself is opaque (captured by the
// lexer's MEDIA_OPEN token), but the nested rules reuse "ruleset" exactly
// like top-level rules, so CssVisitor can flatten them into the same
// CSSRuleSet list - existing selector collection / semantic checks keep
// working unchanged for rules declared inside a media block.
mediaRule: MEDIA_OPEN ruleset* CLOSEBRACE;

ruleset : selector? OPENBRACE properties* CLOSEBRACE;

// A selector is a comma-separated list of complex selectors (grouped
// selectors, e.g. "h1, h2, h3"). Each complex selector is a sequence of
// simple selectors with no structural distinction between a compound run
// (".a.b", no whitespace) and a descendant combination (".a b", whitespace) -
// whitespace is not significant at the grammar level, since CssVisitor
// reconstructs the exact original source text (including any whitespace)
// from the character stream rather than concatenating token text. This
// keeps the grammar simple while still preserving the real selector text
// faithfully, per the "raw selector text is acceptable" allowance.
selector : complexSelector (COMMA_CSS complexSelector)*;

complexSelector : simpleSelector+;

simpleSelector : TAG_SELECTOR | CLASS_SELECTOR | ID_SELECTOR | PSEUDO_ELEMENT | PSEUDO_CLASS;

properties : IDENT COLON_CSS value SEMICOLON_CSS;

// A value is one or more comma-separated fragments, each fragment itself
// being one or more whitespace-separated tokens (e.g. "1px solid #e6ded2",
// "repeat(auto-fit, minmax(240px, 1fr))"). CssVisitor again reconstructs the
// exact original text via source interval, so spacing is preserved.
value : valueFragment+ (COMMA_CSS valueFragment+)*;

valueFragment : STRING_CSS | NUMBER | COLOR | functionCall | IDENT;

// Recursive by construction (functionCall -> value -> valueFragment ->
// functionCall), so CSS functions nest to any depth actually used by real
// stylesheets, e.g. linear-gradient(...) containing rgba(...) calls.
functionCall : IDENT LPAREN_VALUE value? RPAREN_VALUE;
