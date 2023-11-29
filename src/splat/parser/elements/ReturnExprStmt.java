package splat.parser.elements;

import splat.lexer.Token;

public class ReturnExprStmt extends Statement{
    private Expression expr;
    public ReturnExprStmt(Expression expr, Token tok) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }
}
