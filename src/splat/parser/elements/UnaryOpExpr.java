package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

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

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (unaryOp.value.equals("not") && !exprType.getValue().equals("Boolean"))
            throw new SemanticAnalysisException("UnaryOpExpr should be Boolean:" + exprType.getValue(), getLine(), getColumn());
        if (unaryOp.value.equals("-") && !exprType.getValue().equals("Integer"))
            throw new SemanticAnalysisException("UnaryOpExpr should be Integer:" + exprType.getValue(), getLine(), getColumn());

        return exprType;
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value expr_val = expr.evaluate(funcMap, varAndParamMap);

        if (unaryOp.value.equals("not")) {
            return new BoolLiteral(!(boolean) expr_val.getValue());
        } else if (unaryOp.value.equals("-")) {
            return new IntLiteral(-(int)expr_val.getValue());
        }

        throw new ExecutionException("UnaryOp wrong operator", this);
    }
}
