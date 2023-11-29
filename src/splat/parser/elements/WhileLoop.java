package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;
import java.util.Map;

public class WhileLoop extends Statement {

    private Expression expr;
    private List<Statement> stmts;

    public WhileLoop(Expression expr, List<Statement> stmts, Token tok) {
        super(tok);
        this.expr = expr;
        this.stmts = stmts;
    }

    public List<Statement> getStatements() {
        return stmts;
    }

    public Expression getExpression() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {

    }
}
