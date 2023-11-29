package splat.parser.elements;

import splat.lexer.Token;

public class UnaryOpExpr extends Expression{

    private UnaryOp unaryOp;
    private Expression expr;

    public UnaryOpExpr(UnaryOp unaryOp, Expression expr, Token tok) {
        super(tok);
        this.unaryOp = unaryOp;
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }
}
