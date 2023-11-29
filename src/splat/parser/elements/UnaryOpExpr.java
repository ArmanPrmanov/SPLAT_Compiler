package splat.parser.elements;

import splat.lexer.Token;

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
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        return null;
    }
}
