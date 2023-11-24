package splat.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
	private File progFile;

	private List<String> StatementTokenList = Arrays.asList("if", "while", "begin", "end", "program", "do", "return", "then", "is", "print", "print_line");
	private List<String> OperatorTokenList = Arrays.asList("and", "or", "not", ">", "<", ":=", ">=", "<=", "==", "-", "+", "*", "/", "%");
	private List<String> SpecialCharTokenList = Arrays.asList("(", ")", ";", ":", ",");

	private enum LexState
	{
		kInit,
		kFormValue,
		kFormStringValue,
		kFormTwoCharOperator
	}

	public Lexer(File progFile) {
		this.progFile = progFile;
	}

	public List<Token> tokenize() throws LexException {
		if (progFile == null)
			return null;

		List<Token> tokens = new ArrayList<>();
		int line = 1;
		int column = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(progFile))) {
			int currentChar;
			int currTokenLine = 0;
			int currTokenColumn = 0;
			StringBuilder tokenValue = new StringBuilder();

			LexState state = LexState.kInit;

			while ((currentChar = reader.read()) != -1) {
				column++;
				char currentCharValue = (char) currentChar;

				if (state == LexState.kInit) {
					tokenValue = new StringBuilder();

					if (Character.isLetterOrDigit(currentCharValue) || currentCharValue == '_')
					{
						tokenValue.append(currentCharValue);
						state = LexState.kFormValue;
					}
					else if (currentCharValue == '"')
					{
						tokenValue.append(currentCharValue);
						state = LexState.kFormStringValue;
					}
					else if (currentCharValue == ':' || currentCharValue == '=' ||
							currentCharValue == '>' || currentCharValue == '<')
					{
						tokenValue.append(currentCharValue);
						state = LexState.kFormTwoCharOperator;
					}
					else if (isOperator(currentCharValue) || isSpecialChar(currentCharValue))
					{
						tokenValue.append(currentCharValue);
						if (tokenValue.length() > 0) {
							tokens.add(new Token(tokenValue.toString(), line, column));
						}
						state = LexState.kInit;
					}
					else if (currentCharValue == ' ' || currentCharValue == '\t')
					{
					}
					else if (currentCharValue == '\r')
					{
					}
					else if (currentCharValue == '\n')
					{
						tokenValue = new StringBuilder();
						line++;
						column = 0;
					}
					else
					{
						throw new LexException(currentCharValue + "", line, column);
					}
				} else if (state == LexState.kFormValue) {
					if (Character.isLetterOrDigit(currentCharValue) || currentCharValue == '_')
					{
						tokenValue.append(currentCharValue);
					}
					else
					{
						if (tokenValue.length() > 0) {
							tokens.add(new Token(tokenValue.toString(), line, column));
							tokenValue = new StringBuilder();
						}
						if (currentCharValue == '\n')
						{
							tokenValue = new StringBuilder();
							line++;
							column = 0;
						} else if (currentCharValue == '"') {
							state = LexState.kFormStringValue;
							continue;
						} else if (currentCharValue == ':' || currentCharValue == '='
								|| currentCharValue == '<' || currentCharValue == '>'){
							tokenValue.append(currentCharValue);
							state = LexState.kFormTwoCharOperator;
							continue;
						}
						else if (!isOperator(currentCharValue) &&
								!isSpecialChar(currentCharValue) &&
								currentCharValue != ' ' &&
								currentCharValue != '\t' &&
								currentCharValue != '\r'
						)
						{
							throw new LexException(currentCharValue + "", line, column);
						}

						state = LexState.kInit;
					}
				} else if (state == LexState.kFormStringValue) {
					tokenValue.append(currentCharValue);
					if (currentCharValue == '"')
					{
						if (tokenValue.length() > 0) {
							tokens.add(new Token(tokenValue.toString(), line, column));
						}
						state = LexState.kInit;
					}
					else if (currentCharValue == '\n' || currentCharValue == '\r')
					{
						throw new LexException(currentCharValue + "", line, column);
					}
				} else if (state == LexState.kFormTwoCharOperator) {
					char firstChar = tokenValue.charAt(0);
					if ((firstChar == ':' || firstChar == '=' ||
							firstChar == '<' || firstChar == '>') &&
							tokenValue.length() == 1
							&& currentCharValue == '='
					){
						tokenValue.append(currentCharValue);
						tokens.add(new Token(tokenValue.toString(), line, column));
						tokenValue = new StringBuilder();
						state = LexState.kInit;
						continue;
					}
					else if ((firstChar == ':' ||
							firstChar == '<' || firstChar == '>') &&
							tokenValue.length() == 1)
					{
						tokens.add(new Token(tokenValue.toString(), line, column));
						tokenValue = new StringBuilder();
						state = LexState.kInit;

						if (Character.isLetterOrDigit(currentCharValue) || currentCharValue == '_')
						{
							tokenValue.append(currentCharValue);
							state = LexState.kFormValue;
						}
						else if (currentCharValue == '"')
						{
							tokenValue.append(currentCharValue);
							state = LexState.kFormStringValue;
						}
						else if (currentCharValue == ':' || currentCharValue == '=' ||
								currentCharValue == '>' || currentCharValue == '<')
						{
							tokenValue.append(currentCharValue);

							state = LexState.kFormTwoCharOperator;
						}
						else if (isOperator(currentCharValue) || isSpecialChar(currentCharValue))
						{
							tokenValue.append(currentCharValue);
							if (tokenValue.length() > 0) {
								tokens.add(new Token(tokenValue.toString(), line, column));
							}
							state = LexState.kInit;
						}
						else if (currentCharValue == ' ' || currentCharValue == '\t')
						{
						}
						else if (currentCharValue == '\r')
						{
						}
						else if (currentCharValue == '\n')
						{
							tokenValue = new StringBuilder();
							line++;
							column = 0;
						}
						else
						{
							throw new LexException(currentCharValue + "", line, column);
						}
					}
					else {
						throw new LexException(currentCharValue + "", line, column);
					}
				}
			}

			if (tokenValue.length() > 0) {
				tokens.add(new Token(tokenValue.toString(), currTokenLine, currTokenColumn));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//throw new LexException(String.format(tokens.toString()), 0, 0);
		return tokens;
	}

	private boolean isOperator(char ch) {
		return OperatorTokenList.contains(ch+"");
	}

	private boolean isSpecialChar(char ch) {
		return SpecialCharTokenList.contains(ch+"");
	}

	private boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_';
	}
}
