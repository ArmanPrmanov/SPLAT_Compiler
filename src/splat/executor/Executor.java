package splat.executor;

import java.util.HashMap;
import java.util.Map;

import splat.parser.elements.*;

public class Executor {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap = new HashMap<>();
	private Map<String, Value> progVarMap = new HashMap<>();
	
	public Executor(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void runProgram() throws ExecutionException {

		// This sets the maps that will be needed for executing function 
		// calls and storing the values of the program variables
		setMaps();
		
		try {
			
			// Go through and execute each of the statements
			for (Statement stmt : progAST.getStmts()) {
				stmt.execute(funcMap, progVarMap);
			}
			
		// We should never have to catch this exception here, since the
		// main program body cannot have returns
		} catch (ReturnFromCall ex) {
			System.out.println("Internal error!!! The main program body "
					+ "cannot have a return statement -- this should have "
					+ "been caught during semantic analysis!");
			
			throw new ExecutionException("Internal error -- fix your "
					+ "semantic analyzer!", -1, -1);
		}
	}
	
	private void setMaps() {
		// TODO: Use setMaps() from SemanticAnalyzer as a guide

		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();

			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);

			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				Type varType = varDecl.getType();
				if (varType.getValue().equals("Integer")){
					progVarMap.put(label, new IntLiteral(0));
				} else if (varType.getValue().equals("Boolean")){
					progVarMap.put(label, new BoolLiteral(false));
				} else if (varType.getValue().equals("String")){
					progVarMap.put(label, new StringLiteral(""));
				}
			}
		}
	}

}
