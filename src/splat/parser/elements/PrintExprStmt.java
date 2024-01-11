package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class PrintExprStmt extends Statement{
    private Expression expr;
    public PrintExprStmt(Expression expr, Token tok) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        expr.analyzeAndGetType(funcMap, varAndParamMap);
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value exprValue = expr.evaluate(funcMap, varAndParamMap);
        System.out.print(exprValue.getValue().toString());
    }
}
