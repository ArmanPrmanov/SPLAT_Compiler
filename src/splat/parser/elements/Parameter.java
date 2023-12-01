package splat.parser.elements;

import splat.lexer.Token;

public class Parameter extends Declaration{
    // Need to add some fields
    private Type type;

    // Need to add extra arguments for setting fields in the constructor
    public Parameter(String label, Type type, Token tok) {
        super(tok, label);
        this.type = type;
    }

    // Getters?
    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }

    // Fix this as well
    public String toString() {
        String result = "Parameter( \n";
        result = result + "   Label: " + label + "\n";
        result = result + "   Type: " + type + "\n";
        result = result	+ ")";

        return result;
    }
}
