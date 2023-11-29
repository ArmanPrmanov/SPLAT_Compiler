package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class LiteralExpr extends Expression{

    private String value;

    public LiteralExpr (String value, Token tok) {
        super(tok);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        return null;
    }
}
