package splat.parser.elements;

public class Parameter {
    // Need to add some fields
    private String label;
    private Type type;

    // Need to add extra arguments for setting fields in the constructor
    public Parameter(String label, Type type) {
        this.label = label;
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
