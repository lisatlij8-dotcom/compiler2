lexer grammar cssLexer;

// Captures an entire "@media (...) {" prefix as one atomic token, including
// the opening brace, and deliberately does NOT change lexer mode: nested
// rules inside a media block need the same default-mode selector tokens
// (TAG_SELECTOR, CLASS_SELECTOR, ...) as top-level rules, so staying in this
// mode lets the existing ruleset rule be reused recursively for them. Only
// the media condition itself is treated as opaque - full media-query
// semantics are out of scope.
MEDIA_OPEN: '@media' ~[{]* '{';

OPENBRACE : '{'->pushMode(PROPERTYDECLERATON);
CLOSEBRACE : '}';

// Pseudo-element before pseudo-class so '::after' isn't split into ':' + ':after'.
PSEUDO_ELEMENT: '::'[a-zA-Z-]+;
PSEUDO_CLASS: ':'[a-zA-Z-]+;
ID_SELECTOR: '#'[a-zA-Z0-9][a-zA-Z0-9_/-]*;
CLASS_SELECTOR:'.'[a-zA-Z0-9][a-zA-Z0-9_/-]*;
TAG_SELECTOR:[a-zA-Z][a-zA-Z0-9]*;
COMMA_CSS : ',';
CSS_WS: [ \t\r\n]+ -> skip;
CSS_COMMENT: '/*' .*? '*/' -> skip;

mode PROPERTYDECLERATON;
CLOSEBRACE_PROPERTY : '}' -> type(CLOSEBRACE), popMode;
// One general hyphenated-identifier token used for BOTH the property name
// (before COLON_CSS) and bare keyword values / function names (after
// COLON_CSS). Reusing a single token type for both roles - disambiguated
// purely by the parser's grammar position - means there is no competing
// token type for plain keyword text (e.g. "flex", "center", "red"), so the
// PROPERTY-vs-VALUE lexer tie-breaking bug that affected the embedded HTML
// CSS grammar cannot occur here by construction.
IDENT : [a-zA-Z-]+;
STRING_CSS:'"' ~[<"]* '"';
NUMBER : [0-9]+ ('.' [0-9]+)? [a-zA-Z%]*;
COLOR : '#' [0-9a-fA-F]+;
LPAREN_VALUE: '(';
RPAREN_VALUE: ')';
SEMICOLON_CSS : ';';
COLON_CSS : ':';
VALUE_COMMA : ',' -> type(COMMA_CSS);

WS: [ \t\r\n]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
