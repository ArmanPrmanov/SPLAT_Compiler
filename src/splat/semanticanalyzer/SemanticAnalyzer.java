package splat.semanticanalyzer;

import java.util.*;
import java.util.stream.Collectors;

import splat.parser.elements.*;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap = new HashMap<>();
	private Map<String, Type> progVarMap = new HashMap<>();
	
	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {	
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}

		checkProgramBodyForRetStmt(progAST.getStmts());
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(funcMap, varAndParamMap);
		}

		if (funcDecl.getReturnType().type.getValue().equals("void")) {
			checkVoidStmtRetType(funcDecl.getStmts());
		} else {
			checkNonVoidStmtRetType(funcDecl.getStmts(), funcDecl);
		}
	}

	private void checkNonVoidStmtRetType(List<Statement> stmts, FunctionDecl funcDecl) throws SemanticAnalysisException {
		for (Statement stmt : stmts){
			if (stmt instanceof ReturnVoidStmt)
				throw new SemanticAnalysisException("Must return a value", stmt);
		}

		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		var lastStmt = stmts.get(stmts.size() - 1);
		if (lastStmt instanceof ReturnExprStmt) {
			var retStmt = (ReturnExprStmt) lastStmt;
			Type retStmtType = retStmt.getExpr().analyzeAndGetType(funcMap, varAndParamMap);
			if (!retStmtType.getValue().equals(funcDecl.getReturnType().type.getValue()))
				throw new SemanticAnalysisException("Return types different", retStmt);
		} else if (lastStmt instanceof IfThenElse) {
			var ifThenElseStmt = (IfThenElse) lastStmt;
			checkNonVoidStmtRetType(ifThenElseStmt.getThenStatements(), funcDecl);
			checkNonVoidStmtRetType(ifThenElseStmt.getElseStatements(), funcDecl);
		} else if (lastStmt instanceof IfThen) {
			var ifThenStmt = (IfThen) lastStmt;
			checkNonVoidStmtRetType(ifThenStmt.getThenStatements(), funcDecl);
		} else {
			throw new SemanticAnalysisException("ReturnStmt is missing", lastStmt);
		}
	}

	private void checkVoidStmtRetType(List<Statement> stmts) throws SemanticAnalysisException {
		for (Statement stmt : stmts) {
			if (stmt instanceof ReturnExprStmt) {
				throw new SemanticAnalysisException("Must NOT return a value", stmt);
			} else if (stmt instanceof IfThenElse) {
				var ifThenElseStmt = (IfThenElse) stmt;
				checkVoidStmtRetType(ifThenElseStmt.getThenStatements());
				checkVoidStmtRetType(ifThenElseStmt.getElseStatements());
			} else if (stmt instanceof IfThen) {
				var ifThenStmt = (IfThen) stmt;
				checkVoidStmtRetType(ifThenStmt.getThenStatements());
			} else if (stmt instanceof WhileLoop) {
				var whileLoopStmt = (WhileLoop) stmt;
				checkVoidStmtRetType(whileLoopStmt.getStatements());
			}
		}
	}

	private void checkProgramBodyForRetStmt(List<Statement> stmts) throws SemanticAnalysisException{
		for (Statement stmt : stmts){
			if (stmt instanceof ReturnExprStmt || stmt instanceof ReturnVoidStmt) {
				throw new SemanticAnalysisException("Can't have return in program body", stmt);
			} else if (stmt instanceof IfThenElse) {
				var ifThenElseStmt = (IfThenElse) stmt;
				checkProgramBodyForRetStmt(ifThenElseStmt.getThenStatements());
				checkProgramBodyForRetStmt(ifThenElseStmt.getElseStatements());
			} else if (stmt instanceof IfThen) {
				var ifThenStmt = (IfThen) stmt;
				checkProgramBodyForRetStmt(ifThenStmt.getThenStatements());
			} else if (stmt instanceof WhileLoop) {
				var whileLoopStmt = (WhileLoop) stmt;
				checkProgramBodyForRetStmt(whileLoopStmt.getStatements());
			}
		}
	}

	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
		
		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
		Map<String, Type> varAndParamMap = new HashMap<>();

		for (Parameter param : funcDecl.getParams()) {
			String paramName = param.getLabel();
			Type paramType = param.getType();
			varAndParamMap.put(paramName, paramType);
		}

		for (VariableDecl decl : funcDecl.getLocVardecls()) {
			String varName = decl.getLabel();
			Type varType = decl.getType();
			varAndParamMap.put(varName, varType);
		}

		return varAndParamMap;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()
		Set<String> labels = new HashSet<String>();
		for (Declaration decl : progAST.getDecls()){
			if (decl instanceof FunctionDecl){
				var funDecl = (FunctionDecl) decl;
				labels.add(funDecl.getLabel());
			}
		}

		List<Declaration> declarations = new ArrayList<>(funcDecl.getLocVardecls());
		declarations.addAll(funcDecl.getParams());
		for (Declaration decl : declarations) {
			String label = decl.getLabel().toString();

			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
		}
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
