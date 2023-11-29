package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;

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
}
