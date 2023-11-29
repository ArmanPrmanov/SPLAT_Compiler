package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class ReturnVoidStmt extends Statement{
    public ReturnVoidStmt(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {

    }
}
