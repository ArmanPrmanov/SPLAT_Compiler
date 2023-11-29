package splat.parser.elements;

import splat.lexer.Token;

public class LabelExpr extends Expression{
    private String value;

    public LabelExpr(String value, Token tok) {
        super(tok);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
