package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class LabelExpr extends Expression{
    private String value;

    public LabelExpr(String value, Token tok) {
        super(tok);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap)
            throws SemanticAnalysisException {
        if (!varAndParamMap.containsKey(value)){
            throw new SemanticAnalysisException("variable not defined:" + value, getLine(), getColumn());
        } else {
            return varAndParamMap.get(value);
        }
    }
}
