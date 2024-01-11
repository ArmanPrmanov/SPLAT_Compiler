package splat.parser.elements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;

import java.util.Map;

public class PrintLnStmt extends Statement{
    public PrintLnStmt(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {

    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        System.out.println();
    }
}
