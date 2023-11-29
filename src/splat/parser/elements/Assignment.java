package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class Assignment extends Statement {
    private String label;
    private Expression expr;

    public Assignment(String label, Expression expr, Token tok) {
        super(tok);
        this.label = label;
        this.expr = expr;
    }

    public String getLabel() {
        return label;
    }

    public Expression getExpression() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {

    }
}
