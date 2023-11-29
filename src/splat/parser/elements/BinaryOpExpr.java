package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class BinaryOpExpr extends Expression {

    private Expression expr_1;
    private BinaryOp binaryOp;
    private Expression expr_2;

    public BinaryOpExpr(Expression expr_1, Expression expr_2, BinaryOp binaryOp, Token tok) {
        super(tok);
        this.expr_1 = expr_1;
        this.expr_2 = expr_2;
        this.binaryOp = binaryOp;
    }

    public Expression getExpr_1() {
        return expr_1;
    }

    public Expression getExpr_2() {
        return expr_2;
    }

    public BinaryOp getBinaryOp() {
        return binaryOp;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        return null;
    }
}
