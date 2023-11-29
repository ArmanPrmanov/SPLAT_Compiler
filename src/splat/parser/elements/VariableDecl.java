package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

	// Need to add some fields
	private String label;
	private Type type;
	
	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(String label, Type type, Token tok) {
		super(tok);
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
		String result = "VariableDecl( \n";
		result = result + "   Label: " + label + "\n";
		result = result + "   Type: " + type + "\n";
		result = result	+ ")";

		return result;
	}
}
