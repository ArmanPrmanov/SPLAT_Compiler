package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

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

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (!exprType.getValue().equals("Boolean"))
            throw new SemanticAnalysisException("IfThen expr should be Boolean:" + exprType.getValue(), getLine(), getColumn());

        for (Statement stmt : thenStmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }
    }
}
