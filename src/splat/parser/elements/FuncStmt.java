package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncStmt extends Statement{

    private String label;
    private List<Expression> args;

    public FuncStmt(String label, List<Expression> args, Token tok) {
        super(tok);
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }
    public List<Expression> getArguments() {
        return args;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
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
        if (!returnType.type.getValue().equals("void"))
            throw new SemanticAnalysisException("returnType not void:" + returnType.type.getValue(), getLine(), getColumn());
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        FunctionDecl funcDecl = funcMap.get(label);

        Map<String, Value> localVarAndParamMap = new HashMap<>(varAndParamMap);
        List<Parameter> params = funcDecl.getParams();

        for (int i = 0; i < args.size(); i++) {
            Value argValue = args.get(i).evaluate(funcMap, localVarAndParamMap);
            localVarAndParamMap.put(params.get(i).getLabel(), argValue);
        }
        try {
            for (Statement stmt : funcDecl.getStmts()) {
                stmt.execute(funcMap, localVarAndParamMap);
            }
        } catch (ReturnFromCall retFromCall){
            if (retFromCall.getReturnVal() != null)
                throw new ExecutionException("Return value not null in FuncStmt:" + retFromCall.getReturnVal(), this);
        }
    }
}
