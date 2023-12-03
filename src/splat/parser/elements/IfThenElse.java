package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

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

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (!exprType.getValue().equals("Boolean"))
            throw new SemanticAnalysisException("IfThenElse expr should be Boolean:" + exprType.getValue(), getLine(), getColumn());

        for (Statement stmt : thenStmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }

        for (Statement stmt : elseStmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value exprValue = expr.evaluate(funcMap, varAndParamMap);
        if ((boolean)exprValue.getValue()) {
            for (Statement stmt : thenStmts) {
                stmt.execute(funcMap, varAndParamMap);
            }
        } else {
            for (Statement stmt : elseStmts) {
                stmt.execute(funcMap, varAndParamMap);
            }
        }
    }
}
