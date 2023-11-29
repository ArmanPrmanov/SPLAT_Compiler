package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class LabelExpr extends Expression{
    private String value;

    public LabelExpr(String value, Token tok) {
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
