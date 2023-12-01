package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class LiteralExpr extends Expression{

    private String value;

    public LiteralExpr (String value, Token tok) {
        super(tok);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
            throws SemanticAnalysisException {

        if (isIntLiteral(value))
            return new Type("Integer");

        if (isBoolLiteral(value))
            return new Type("Boolean");

        if (isStringLiteral(value))
            return new Type("String");

        //UNREACHABLE EXCEPTION WOW
        throw new SemanticAnalysisException("Literal not defined WOW:" + value, getLine(), getColumn());
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        return null;
    }


    private boolean isLiteral(String value) {
        return isIntLiteral(value) || isBoolLiteral(value) || isStringLiteral(value);
    }

    private boolean isIntLiteral(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))){
                return false;
            }
        }

        return true;
    }

    private boolean isBoolLiteral(String value) {
        return value.equals("true") || value.equals("false");
    }

    private boolean isStringLiteral(String value) {
        return value.charAt(0) == '"' || value.charAt(value.length() - 1) == '"';
    }
}
