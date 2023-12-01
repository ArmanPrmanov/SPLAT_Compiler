package splat.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jdk.jshell.spi.ExecutionControl;
import splat.lexer.Token;
import splat.parser.elements.*;

public class Parser {

	private List<Token> tokens;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			
			checkNext("program");
			
			List<Declaration> decls = parseDecls();
			
			checkNext("begin");
			
			List<Statement> stmts = parseStmts();
			
			checkNext("end");
			checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {

		Token startTok = tokens.get(0);

		String label = parseLabel();

		checkNext("(");

		List<Parameter> params = parseParams();

		checkNext(")");

		checkNext(":");

		ReturnType retType = parseReturnType();

		checkNext("is");

		List<VariableDecl> locVarDecls = parseLocVarDecls();

		checkNext("begin");

		List<Statement> stmts = parseStmts();

		checkNext("end");
		checkNext(";");

		return new FunctionDecl(label, params, retType, locVarDecls, stmts, startTok);
	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		//throw new ParseException(String.format(tokens.toString()), 0, 0);
		Token startTok = tokens.get(0);

		String label = parseLabel();

		checkNext(":");

		Type type = parseType();

		checkNext(";");

		return new VariableDecl(label, type, startTok);
	}

	/*
	 * <params> ::= <param> ( , <param> )* | ɛ
	 */
	private List<Parameter> parseParams() throws ParseException {

		List<Parameter> params = new ArrayList<Parameter>();

		while (!peekNext(")")) {
			if (peekNext(",")) {
				checkNext(",");
				continue;
			}

			Parameter param = parseParam();
			params.add(param);
		}

		return params;
	}

	/*
	 * <param> ::= <label> : <type>
	 */
	private Parameter parseParam() throws ParseException {

		String label = parseLabel();

		checkNext(":");

		Type type = parseType();

		return new Parameter(label, type, new Token("null", 0, 0));
	}

	/*
	 *  <loc-var-decls> ::= ( <var-decl> )*
	 */
	private List<VariableDecl> parseLocVarDecls() throws ParseException {

		List<VariableDecl> decls = new ArrayList<VariableDecl>();

		while (!peekNext("begin")) {
			VariableDecl decl = parseVarDecl();
			decls.add(decl);
		}

		return decls;
	}
	
	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {
		List<Statement> stmts = new ArrayList<Statement>();

		while (!peekNext("end")) {
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}

		return stmts;
	}

	private List<Statement> parseThenStmts() throws ParseException {
		List<Statement> stmts = new ArrayList<Statement>();

		while (!peekNext("else") && !peekNext("end")) {
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}

		return stmts;
	}

	/*
	 * <stmt> ::= <label> := <expr> ;
	          	| while <expr> do <stmts> end while ;
	          	| if <expr> then <stmts> else <stmts> end if ;
	          	| if <expr> then <stmts> end if ;
	          	| <label> ( <args> ) ;
	          	| print <expr> ;
	          	| print_line ;
	          	| return <expr> ;
	          	| return ;
	 */
	private Statement parseStmt() throws ParseException {
		Token tok = tokens.get(0);
		String value = tok.getValue();

		if (value.equals("return")) {
			if (peekTwoAhead(";")){
				checkNext("return");
				checkNext(";");
				return new ReturnVoidStmt(tok);
			} else {
				checkNext("return");
				Expression expr = parseExpr();
				checkNext(";");
				return new ReturnExprStmt(expr, tok);
			}
		} else if (value.equals("print")) {
			checkNext("print");
			Expression expr = parseExpr();
			checkNext(";");
			return new PrintExprStmt(expr, tok);
		} else if (value.equals("print_line")){
			checkNext("print_line");
			checkNext(";");
			return new PrintLnStmt(tok);
		} else if (value.equals("while")){
			checkNext("while");
			Expression expr = parseExpr();
			checkNext("do");
			List<Statement> stmts = parseStmts();
			checkNext("end");
			checkNext("while");
			checkNext(";");
			return new WhileLoop(expr, stmts, tok);
		} else if (value.equals("if")){
			checkNext("if");
			Expression expr = parseExpr();
			checkNext("then");
			List<Statement> thenStmts = parseThenStmts();
			if (peekNext("else")){
				checkNext("else");
				List<Statement> elseStmts = parseStmts();
				checkNext("end");
				checkNext("if");
				checkNext(";");
				return new IfThenElse(expr, thenStmts, elseStmts, tok);
			} else if (peekNext("end")){
				checkNext("end");
				checkNext("if");
				checkNext(";");
				return new IfThen(expr, thenStmts, tok);
			} else {
				throw new ParseException("IfThen?Else_Stmt expected: " + tokens.get(0).getValue(), tok);
			}
		} else if (isLabel(value)){
			if (peekTwoAhead(":=")) {
				String label = parseLabel();
				checkNext(":=");
				Expression expr = parseExpr();
				checkNext(";");
				return new Assignment(label, expr, tok);
			} else if (peekTwoAhead("(")) {
				String label = parseLabel();
				checkNext("(");
				List<Expression> args = parseArgs();
				checkNext(")");
				checkNext(";");
				return new FuncStmt(label, args, tok);
			} else{
				throw new ParseException("Label Stmt expected: " + tokens.get(0).getValue(), tok);
			}
		}
		else {
			throw new ParseException("Statement expected", tok);
		}
	}

	/*
	 * <expr> ::= ( <expr> <bin-op> <expr> )
	 *  		| ( <unary-op> <expr> )
				| <label> ( <args> )
				| <label>
				| <literal>
	 */
	private Expression parseExpr() throws ParseException {
		Token tok = tokens.get(0);
		String value = tok.getValue();

		// ( <expr> <bin-op> <expr> )
		// ( <unary-op> <expr> )
		if (value.equals("(")) {
			Token twoAhead = tokens.get(1);
			if (isUnaryOp(twoAhead.getValue())){
				return parseUnaryOpExpr();
			} else {
				return parseBinaryOpExpr();
			}
		} else if (isLiteral(value)){
			tokens.remove(0);
			return new LiteralExpr(value, tok);
		} else if (isLabel(value)){
			if (peekTwoAhead("(")) {
				String label = parseLabel();
				checkNext("(");
				List<Expression> args = parseArgs();
				checkNext(")");
				return new FuncExpr(label, args, tok);
			} else {
				String label = parseLabel();
				return new LabelExpr(label, tok);
			}
		} else {
			throw new ParseException("Expression expected", tok);
		}
	}

	private UnaryOpExpr parseUnaryOpExpr() throws ParseException {
		Token tok = tokens.get(0);

		checkNext("(");

		UnaryOp unaryOp;
		String unaryOpValue = tokens.remove(0).getValue();
		if (!isUnaryOp(unaryOpValue))
			throw new ParseException("Unary-Op expected", tok);
		else
			unaryOp = new UnaryOp(unaryOpValue);

		Expression expr = parseExpr();

		checkNext(")");

		return new UnaryOpExpr(unaryOp, expr, tok);
	}

	private BinaryOpExpr parseBinaryOpExpr() throws ParseException {
		Token tok = tokens.get(0);

		checkNext("(");

		Expression expr_1 = parseExpr();

		BinaryOp binaryOp;
		Token binOpToken = tokens.remove(0);
		String binaryOpValue = binOpToken.getValue();
		if (!isBinaryOp(binaryOpValue))
			//throw new ParseException(String.format(tokens.toString()), 0, 0);
			throw new ParseException(String.format("Binary-Op expected {%s}", binOpToken.toString()), tok);
		else
			binaryOp = new BinaryOp(binaryOpValue);

		Expression expr_2 = parseExpr();

		checkNext(")");

		return new BinaryOpExpr(expr_1, expr_2, binaryOp, tok);
	}


	/*
	 * <args> ::= <expr> ( , <expr> )* | ɛ
	 */
	private List<Expression> parseArgs() throws ParseException {

		List<Expression> args = new ArrayList<Expression>();

		while (!peekNext(")")) {
			if (peekNext(",")) {
				checkNext(",");
				continue;
			}

			Expression arg = parseExpr();
			args.add(arg);
		}

		return args;
	}

	/*
	 * <label> ::= ...sequence of alphanumeric characters and underscore, not starting with a digit,
 	               which are not keywords...
	 */
	private String parseLabel() throws ParseException {
		Token tok = tokens.remove(0);
		String label = tok.getValue();

		if (Character.isDigit(label.charAt(0))){
			throw new ParseException("Label expected", tok);
		}

		for (int i = 0; i < label.length(); i++) {
			if (!Character.isLetterOrDigit(label.charAt(i)) && label.charAt(i) != '_'){
				throw new ParseException("Label expected", tok);
			}
		}

		return label;
	}

	/*
	 * <ret-type> ::= <type> | void
	 */
	private ReturnType parseReturnType() throws ParseException {
		ReturnType retType;

		if (peekNext("void")){
			checkNext("void");
			retType = new ReturnType(new Type("void"));
		} else {
			Type type = parseType();
			retType = new ReturnType(type);
		}
		return retType;
	}

	/*
	 * <type> ::= Integer | Boolean | String
	 */
	private Type parseType() throws ParseException {
		Token tok = tokens.remove(0);
		String value = tok.getValue();

		if (value.equals("Integer") || value.equals("Boolean") || value.equals("String")){
			return new Type(value);
		} else {
			throw new ParseException("Type expected: " + value, tok);
		}
	}

	/*
	 * <literal> ::= <int-literal> | <bool-literal> | <string-literal>
	 */
	private String parseLiteral() throws ParseException {
		Token tok = tokens.remove(0);
		String value = tok.getValue();

		if (isIntLiteral(value)){
			return parseIntLiteral();
		} else if (isBoolLiteral(value)){
			return parseBoolLiteral();
		} else if (isStringLiteral(value)) {
			return parseStringLiteral();
		} else {
			throw new ParseException("Literal expected", tok);
		}
	}

	/*
	 * <int-literal> ::= ...sequence of decimal digits...
	 */
	private String parseIntLiteral() throws ParseException {
		Token tok = tokens.remove(0);
		String value = tok.getValue();

		for (int i = 0; i < value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))){
				throw new ParseException("IntLiteral expected", tok);
			}
		}

		return value;
	}

	/*
	 * <bool-literal> ::= true | false
	 */
	private String parseBoolLiteral() throws ParseException {
		Token tok = tokens.remove(0);
		String value = tok.getValue();

		if (value != "true" && value != "false")
			throw new ParseException("BoolLiteral expected", tok);

		return value;
	}

	/*
	 * <string-literal> ::= "...sequence of characters and space that do not contain double-quotes, backslashes,
	                        or newlines..."
	 */
	private String parseStringLiteral() throws ParseException {
		Token tok = tokens.remove(0);
		String value = tok.getValue();

		if (value.charAt(0) != '"' && value.charAt(value.length()-1) != '"')
			throw new ParseException("BoolLiteral expected", tok);

		return value;
	}


	private List<String> Keywords = Arrays.asList("if", "while", "begin", "end", "program", "do", "return", "then", "is", "print", "print_line");
	private List<String> BinaryOps = Arrays.asList("and", "or", ">", "<", "==", ">=", "<=", "+", "-", "*", "/", "%");
	private List<String> UnaryOps = Arrays.asList("not", "-");

	private boolean isBinaryOp(String value) {
		return BinaryOps.contains(value);
	}

	private boolean isUnaryOp(String value) {
		return UnaryOps.contains(value);
	}

	private boolean isLabel(String value) {
		if (Keywords.contains(value))
			return false;

		if (Character.isDigit(value.charAt(0)))
			return false;

		for (int i = 0; i < value.length(); i++) {
			if (!Character.isLetterOrDigit(value.charAt(i)) && value.charAt(i) != '_'){
				return false;
			}
		}

		return true;
	}

	private boolean isLiteral(String value) {
		return isIntLiteral(value) || isBoolLiteral(value) || isStringLiteral(value);
	}

	private boolean isIntLiteral(String value) {
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))){
				return false;
			}
		}

		return true;
	}

	private boolean isBoolLiteral(String value) {
        return value.equals("true") || value.equals("false");
	}

	private boolean isStringLiteral(String value) {
        return value.charAt(0) == '"' || value.charAt(value.length() - 1) == '"';
	}
}
