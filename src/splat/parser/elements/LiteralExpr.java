package splat.parser.elements;

import splat.lexer.Token;

public class LiteralExpr extends Expression{

    private String value;

    public LiteralExpr (String value, Token tok) {
        super(tok);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
