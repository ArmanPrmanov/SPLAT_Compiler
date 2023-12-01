package splat.parser.elements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

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
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (!exprType.getValue().equals("Boolean"))
            throw new SemanticAnalysisException("WhileLoop expr should be Boolean:" + exprType.getValue(), getLine(), getColumn());

        for (Statement stmt : stmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {

    }
}
