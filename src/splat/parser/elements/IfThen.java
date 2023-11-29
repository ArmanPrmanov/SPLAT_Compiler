package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;

public class IfThen extends Statement{

    private Expression expr;
    private List<Statement> thenStmts;

    public IfThen(Expression expr, List<Statement> thenStmts, Token tok) {
        super(tok);
        this.expr = expr;
        this.thenStmts = thenStmts;
    }

    public Expression getExpression() {
        return expr;
    }
    public List<Statement> getThenStatements() {
        return thenStmts;
    }
}
