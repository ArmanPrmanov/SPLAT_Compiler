package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class ReturnExprStmt extends Statement{
    private Expression expr;
    public ReturnExprStmt(Expression expr, Token tok) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {

    }
}
