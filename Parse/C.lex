package Parse;
import ErrorMsg.ErrorMsg;

%%

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
private void newline() { errorMsg.newline(yychar); }
private void err(int pos, String s) { errorMsg.error(pos, s); }
private void err(String s) { err(yychar, s); }

private java_cup.runtime.Symbol tok(int kind) { return tok(kind, null); }
private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar + yylength(), value);
}

private ErrorMsg errorMsg;

Yylex(java.io.InputStream s, ErrorMsg e) { this(s); errorMsg = e; }

StringBuffer s = new StringBuffer();
%}

%state COMMENT

%eofval{
{
    return tok(sym.EOF, null);
}
%eofval}

DIGITS = [0-9]+
ALPHA  = [a-zA-Z_]

%%

<YYINITIAL> "/*"       { yybegin(COMMENT); }
<COMMENT>  [^*\n]+     { ; }
<COMMENT>  "*"+[^*/\n] { ; }
<COMMENT>  \n          { newline(); }
<COMMENT>  "*"+"/"     { yybegin(YYINITIAL); }

<YYINITIAL> "//"[^\n]* { ; }
<YYINITIAL> [\ \t\r]+  { ; }
<YYINITIAL> \n         { newline(); }

<YYINITIAL> "auto"     { return tok(sym.AUTO, null); }
<YYINITIAL> "break"    { return tok(sym.BREAK, null); }
<YYINITIAL> "case"     { return tok(sym.CASE, null); }
<YYINITIAL> "char"     { return tok(sym.CHAR, null); }
<YYINITIAL> "const"    { return tok(sym.CONST, null); }
<YYINITIAL> "continue" { return tok(sym.CONTINUE, null); }
<YYINITIAL> "default"  { return tok(sym.DEFAULT, null); }
<YYINITIAL> "do"       { return tok(sym.DO, null); }
<YYINITIAL> "double"   { return tok(sym.DOUBLE, null); }
<YYINITIAL> "else"     { return tok(sym.ELSE, null); }
<YYINITIAL> "enum"     { return tok(sym.ENUM, null); }
<YYINITIAL> "extern"   { return tok(sym.EXTERN, null); }
<YYINITIAL> "float"    { return tok(sym.FLOAT, null); }
<YYINITIAL> "for"      { return tok(sym.FOR, null); }
<YYINITIAL> "fun"      { return tok(sym.FUN, null); }
<YYINITIAL> "goto"     { return tok(sym.GOTO, null); }
<YYINITIAL> "if"       { return tok(sym.IF, null); }
<YYINITIAL> "int"      { return tok(sym.INT, null); }
<YYINITIAL> "long"     { return tok(sym.LONG, null); }
<YYINITIAL> "register" { return tok(sym.REGISTER, null); }
<YYINITIAL> "return"   { return tok(sym.RETURN, null); }
<YYINITIAL> "short"    { return tok(sym.SHORT, null); }
<YYINITIAL> "signed"   { return tok(sym.SIGNED, null); }
<YYINITIAL> "sizeof"   { return tok(sym.SIZEOF, null); }
<YYINITIAL> "static"   { return tok(sym.STATIC, null); }
<YYINITIAL> "struct"   { return tok(sym.STRUCT, null); }
<YYINITIAL> "switch"   { return tok(sym.SWITCH, null); }
<YYINITIAL> "typedef"  { return tok(sym.TYPEDEF, null); }
<YYINITIAL> "union"    { return tok(sym.UNION, null); }
<YYINITIAL> "unsigned" { return tok(sym.UNSIGNED, null); }
<YYINITIAL> "var"      { return tok(sym.VAR, null); }
<YYINITIAL> "void"     { return tok(sym.VOID, null); }
<YYINITIAL> "volatile" { return tok(sym.VOLATILE, null); }
<YYINITIAL> "while"    { return tok(sym.WHILE, null); }

<YYINITIAL> "<<="  { return tok(sym.LSHIFTASSIGN, null); }
<YYINITIAL> ">>="  { return tok(sym.RSHIFTASSIGN, null); }
<YYINITIAL> "+="   { return tok(sym.ADDASSIGN, null); }
<YYINITIAL> "-="   { return tok(sym.SUBASSIGN, null); }
<YYINITIAL> "*="   { return tok(sym.MULASSIGN, null); }
<YYINITIAL> "/="   { return tok(sym.DIVASSIGN, null); }
<YYINITIAL> "%="   { return tok(sym.MODASSIGN, null); }
<YYINITIAL> "&="   { return tok(sym.BWISEANDASSIGN, null); }
<YYINITIAL> "|="   { return tok(sym.BWISEORASSIGN, null); }
<YYINITIAL> "^="   { return tok(sym.BWISEXORASSIGN, null); }

<YYINITIAL> "<<"   { return tok(sym.LSHIFT, null); }
<YYINITIAL> ">>"   { return tok(sym.RSHIFT, null); }
<YYINITIAL> "=="   { return tok(sym.EQ, null); }
<YYINITIAL> "!="   { return tok(sym.NEQ, null); }
<YYINITIAL> "<="   { return tok(sym.LE, null); }
<YYINITIAL> ">="   { return tok(sym.GE, null); }
<YYINITIAL> "&&"   { return tok(sym.AND, null); }
<YYINITIAL> "||"   { return tok(sym.OR, null); }
<YYINITIAL> "++"   { return tok(sym.INC, null); }
<YYINITIAL> "--"   { return tok(sym.DEC, null); }
<YYINITIAL> "->"   { return tok(sym.ARROW, null); }

<YYINITIAL> ","    { return tok(sym.COMMA, null); }
<YYINITIAL> ";"    { return tok(sym.SEMICOLON, null); }
<YYINITIAL> ":"    { return tok(sym.COLON, null); }
<YYINITIAL> "."    { return tok(sym.DOT, null); }
<YYINITIAL> "?"    { return tok(sym.QUESTION, null); }

<YYINITIAL> "("    { return tok(sym.LPAREN, null); }
<YYINITIAL> ")"    { return tok(sym.RPAREN, null); }
<YYINITIAL> "{"    { return tok(sym.LBRACE, null); }
<YYINITIAL> "}"    { return tok(sym.RBRACE, null); }
<YYINITIAL> "["    { return tok(sym.LBRACK, null); }
<YYINITIAL> "]"    { return tok(sym.RBRACK, null); }

<YYINITIAL> "+"    { return tok(sym.PLUS, null); }
<YYINITIAL> "-"    { return tok(sym.MINUS, null); }
<YYINITIAL> "*"    { return tok(sym.TIMES, null); }
<YYINITIAL> "/"    { return tok(sym.DIVIDE, null); }
<YYINITIAL> "%"    { return tok(sym.MOD, null); }
<YYINITIAL> "<"    { return tok(sym.LT, null); }
<YYINITIAL> ">"    { return tok(sym.GT, null); }
<YYINITIAL> "="    { return tok(sym.ASSIGN, null); }
<YYINITIAL> "!"    { return tok(sym.NOT, null); }
<YYINITIAL> "&"    { return tok(sym.BITAND, null); }
<YYINITIAL> "|"    { return tok(sym.BITOR, null); }
<YYINITIAL> "^"    { return tok(sym.BITXOR, null); }
<YYINITIAL> "~"    { return tok(sym.BITNOT, null); }

<YYINITIAL> {ALPHA}({ALPHA}|{DIGITS})* { return tok(sym.ID, yytext()); }
<YYINITIAL> {DIGITS}                    { return tok(sym.INTLIT, Integer.valueOf(yytext())); }

<YYINITIAL> \"[^\"]*\"  { return tok(sym.STRLIT, yytext().substring(1, yytext().length()-1)); }
<YYINITIAL> \'[^\']\'   { return tok(sym.CHARLIT, yytext().substring(1, 2)); }

<YYINITIAL> "#"[^\n]*   { ; }
<YYINITIAL> .           { err("Illegal character: " + yytext()); }
