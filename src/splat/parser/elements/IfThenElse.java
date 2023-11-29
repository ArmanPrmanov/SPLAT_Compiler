package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;

public class IfThenElse extends Statement{

    private Expression expr;
    private List<Statement> thenStmts;
    private List<Statement> elseStmts;

    public IfThenElse(Expression expr, List<Statement> thenStmts, List<Statement> elseStmts, Token tok) {
        super(tok);
        this.expr = expr;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }

    public Expression getExpression() {
        return expr;
    }
    public List<Statement> getThenStatements() {
        return thenStmts;
    }
    public List<Statement> getElseStatements() {
        return thenStmts;
    }
}
