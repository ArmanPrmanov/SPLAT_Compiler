package splat.parser.elements;

import splat.lexer.Token;

public class PrintExprStmt extends Statement{
    private Expression expr;
    public PrintExprStmt(Expression expr, Token tok) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }
}
