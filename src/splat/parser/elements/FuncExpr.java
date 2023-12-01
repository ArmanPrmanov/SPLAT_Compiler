package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuncExpr extends Expression{

    private String label;
    private List<Expression> args;

    public FuncExpr(String label, List<Expression> args, Token tok) {
        super(tok);
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }
    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!funcMap.containsKey(label))
            throw new SemanticAnalysisException("variable not defined:" + label, getLine(), getColumn());

        if (funcMap.get(label).getParams().size() != args.size())
            throw new SemanticAnalysisException("params length mismatch:" + label, getLine(), getColumn());

        List<Parameter> params = funcMap.get(label).getParams();
        for (int i = 0; i < args.size(); i++) {
            Type argType = args.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if (!params.get(i).getType().getValue().equals(argType.getValue())){
                throw new SemanticAnalysisException("param Type mismatch:" + argType.getValue(), getLine(), getColumn());
            }
        }

        ReturnType returnType = funcMap.get(label).getReturnType();
        if (returnType.type.getValue().equals("void"))
            throw new SemanticAnalysisException("returnType void:" + returnType.type.getValue(), getLine(), getColumn());

        return returnType.type;
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        return null;
    }
}
