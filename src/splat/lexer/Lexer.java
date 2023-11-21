package splat.lexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
	private File progFile;

	private List<String> StatementTokenList = Arrays.asList("if", "while", "begin", "end", "program", "do", "return", "then", "is", "print", "print_line");
	private List<String> OperatorTokenList = Arrays.asList("and", "or", "not", ">", "<", ">=", "<=", "=", "==", "-", "+", "*", "/", "%");
	private List<String> SpecialCharTokenList = Arrays.asList("(", ")", ";", ":", ",", "\"", "!");

	public Lexer(File progFile) {
		this.progFile = progFile;
	}

	public List<Token> tokenize() throws LexException {
		if (progFile == null)
			return null;

		List<Token> tokens = new ArrayList<>();
		int line = 1;
		int column = 0;

		try (FileReader reader = new FileReader(progFile)) {
			int currentChar;
			int currTokenLine = 0;
			int currTokenColumn = 0;
			StringBuilder tokenValue = new StringBuilder();

			while ((currentChar = reader.read()) != -1) {
				column++;
				char currentCharValue = (char) currentChar;

				if (currentCharValue == '\n' || currentCharValue == '\r') {
					line++;
					column = 0;
					continue;
				}


				if (currentCharValue == ' ' || currentCharValue == '\t') {
					if (tokenValue.length() > 0) {
						tokens.add(new Token(tokenValue.toString(), currTokenLine, currTokenColumn));
						tokenValue = new StringBuilder();
					}
				}
				else if (isSpecialChar(currentCharValue)){
					if (tokenValue.length() > 0) {
						tokens.add(new Token(tokenValue.toString(), currTokenLine, currTokenColumn));
						tokenValue = new StringBuilder();
					}
					tokens.add(new Token(currentCharValue + "", currTokenLine, currTokenColumn));
					tokenValue = new StringBuilder();
				}
				else if (isValidCharacter(currentCharValue) ||
						isOperator(currentCharValue)) {
					if (tokenValue.length() == 0) {
						currTokenLine = line;
						currTokenColumn = column;
					}
					tokenValue.append(currentCharValue);
				} else {
					throw new LexException(currentCharValue + "", line, column);
				}
			}

			if (tokenValue.length() > 0) {
				tokens.add(new Token(tokenValue.toString(), currTokenLine, currTokenColumn));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
