package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

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
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type expr1Type = expr_1.analyzeAndGetType(funcMap, varAndParamMap);
        Type expr2Type = expr_2.analyzeAndGetType(funcMap, varAndParamMap);

        if ((binaryOp.value.equals("and") || binaryOp.value.equals("or"))
                && (!expr1Type.getValue().equals("Boolean") || !expr2Type.getValue().equals("Boolean")))
            throw new SemanticAnalysisException(
                    "BinaryOpExpr should be Boolean:" + expr1Type.getValue() + "," + expr2Type.getValue(), getLine(), getColumn());

        if ((binaryOp.value.equals("+") || binaryOp.value.equals("-") || binaryOp.value.equals("*") || binaryOp.value.equals("/") || binaryOp.value.equals("%"))
                && (!expr1Type.getValue().equals("Integer") || !expr2Type.getValue().equals("Integer")))
            throw new SemanticAnalysisException(
                    "BinaryOpExpr should be Integer:" + expr1Type.getValue() + "," + expr2Type.getValue(), getLine(), getColumn());

        if ((binaryOp.value.equals(">") || binaryOp.value.equals("<") || binaryOp.value.equals(">=") || binaryOp.value.equals("<="))
                && (!expr1Type.getValue().equals("Integer") || !expr2Type.getValue().equals("Integer")))
            throw new SemanticAnalysisException(
                    "BinaryOpExpr should be Integer:" + expr1Type.getValue() + "," + expr2Type.getValue(), getLine(), getColumn());

        if ((binaryOp.value.equals("==")) && !expr1Type.getValue().equals(expr2Type.getValue()))
            throw new SemanticAnalysisException(
                    "BinaryOpExpr types must match:" + expr1Type.getValue() + "," + expr2Type.getValue(), getLine(), getColumn());

        if (binaryOp.value.equals("-") || binaryOp.value.equals("+") || binaryOp.value.equals("*") || binaryOp.value.equals("/") || binaryOp.value.equals("%"))
            return new Type("Integer");
        else
            return new Type("Boolean");
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value expr1_val = expr_1.evaluate(funcMap, varAndParamMap);
        Value expr2_val = expr_2.evaluate(funcMap, varAndParamMap);

        if (binaryOp.value.equals(">")){
            return new IntLiteral((int)expr1_val.getValue() > (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("<")){
            return new IntLiteral((int)expr1_val.getValue() < (int)expr2_val.getValue());
        } else if (binaryOp.value.equals(">=")){
            return new IntLiteral((int)expr1_val.getValue() >= (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("<=")){
            return new IntLiteral((int)expr1_val.getValue() <= (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("+")){
            return new IntLiteral((int)expr1_val.getValue() + (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("-")){
            return new IntLiteral((int)expr1_val.getValue() - (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("*")){
            return new IntLiteral((int)expr1_val.getValue() * (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("/")){
            var secondValue = (int)expr2_val.getValue();
            if (secondValue == 0)
                throw new ExecutionException("Division by zero", this);
            return new IntLiteral((int)expr1_val.getValue() / secondValue);
        } else if (binaryOp.value.equals("%")){
            return new IntLiteral((int)expr1_val.getValue() % (int)expr2_val.getValue());
        } else if (binaryOp.value.equals("and")){
            return new BoolLiteral((boolean)expr1_val.getValue() && (boolean)expr2_val.getValue());
        } else if (binaryOp.value.equals("or")){
            return new BoolLiteral((boolean)expr1_val.getValue() || (boolean)expr2_val.getValue());
        } else if (binaryOp.value.equals("==")){
            return new BoolLiteral(expr1_val.getValue() == expr2_val.getValue());
        }

        throw new ExecutionException("BinOp wrong operator", this);
    }
}
