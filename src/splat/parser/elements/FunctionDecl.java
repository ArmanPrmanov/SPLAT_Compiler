package splat.parser.elements;
import splat.lexer.Token;
import java.util.List;

public class FunctionDecl extends Declaration {

	// Need to add some fields
	private List<Parameter> params;
	private ReturnType returnType;
	private List<VariableDecl> locVardecls;
	private List<Statement> stmts;
	
	// Need to add extra arguments for setting fields in the constructor 
	public FunctionDecl(String label,
						List<Parameter> params,
						ReturnType returnType,
						List<VariableDecl> locVardecls,
						List<Statement> stmts,
						Token tok) {
		super(tok, label);
		this.params = params;
		this.returnType = returnType;
		this.locVardecls = locVardecls;
		this.stmts = stmts;
	}

	public List<Statement> getStmts() {
		return stmts;
	}
	
	// Fix this as well
	public String toString() {
		String result = "FunctionDecl( \n";
		result = result + "   Label: " + label + "\n";
		result = result + "( \n";
		result = result + "   Params: " + params + "\n";
		result = result + ") : \n";
		result = result + "   ReturnType: " + returnType + "\n";
		result = result + "is locVardecls \n";
		for (VariableDecl decl : locVardecls) {
			result = result + "   " + decl + "\n";
		}
		result = result + "stmts \n";
		for (Statement stmt : stmts) {
			result = result + "   " + stmt + "\n";
		}
		result = result	+ ")";

		return result;
	}
}
