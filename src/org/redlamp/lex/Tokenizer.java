package org.redlamp.lex;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.redlamp.lex.Token.TokenClass;

public class Tokenizer {

	Scanner scanner;
	final Map<String, TokenClass> keywords = new HashMap<String, TokenClass>();

	public Tokenizer(InputStream inputStream) {
		scanner = new Scanner(inputStream);
		initKeywords();
	}

	private void initKeywords() {
		keywords.put("insert", TokenClass.KEYWORD);
		keywords.put("database", TokenClass.KEYWORD);
		keywords.put("use", TokenClass.KEYWORD);
		keywords.put("select", TokenClass.KEYWORD);
		keywords.put("into", TokenClass.KEYWORD);
		keywords.put("from", TokenClass.KEYWORD);
		keywords.put("where", TokenClass.KEYWORD);
		keywords.put("not", TokenClass.KEYWORD);
		keywords.put("null", TokenClass.KEYWORD);
		keywords.put("values", TokenClass.KEYWORD);
		keywords.put("delete", TokenClass.KEYWORD);
		keywords.put("now", TokenClass.KEYWORD);
		keywords.put("is", TokenClass.KEYWORD);
	}

	public Tokenizer(String inputString) {
		scanner = new Scanner(inputString);
		initKeywords();
	}

	Token next() {

		char c = scanner.next();

		if (Character.isWhitespace(c))
			return next();

		// operators
		TokenClass ident = ident(c);
		if (ident != TokenClass.NONE) {
			return new Token(ident, Character.toString(c));
		} else if (ident == TokenClass.END) {
			return null;
		}

		// identifier
		if (Character.isLetter(c)) {
			StringBuilder builder = new StringBuilder();
			builder.append(c);
			c = scanner.next();
			while (Character.isLetterOrDigit(c) || c == '_') {
				builder.append(c);
				c = scanner.next();
			}
			if (keywords.containsKey(builder.toString().toLowerCase())) {
				return new Token(TokenClass.KEYWORD, builder.toString());
			}
			return new Token(TokenClass.IDENT, builder.toString());
		}
		// number
		if (Character.isDigit(c)) {
			StringBuilder builder = new StringBuilder();
			builder.append(c);
			c = scanner.next();
			while (Character.isDigit(c)) {
				builder.append(c);
				c = scanner.next();
			}
			return new Token(TokenClass.NUMBER, builder.toString());
		}
		return null;
	}

	private TokenClass ident(char c) {
		switch (c) {
		case '+':
			return TokenClass.PLUS;
		case '-':
			return TokenClass.MINUS;
		case '*':
			return TokenClass.MULT;
		case '/':
			return TokenClass.DIV;
		case '=':
			return TokenClass.EQ;
		case ')':
			return TokenClass.RPAREN;
		case '(':
			return TokenClass.LPAREN;
		case ';':
			return TokenClass.END;
		case '_':
			return TokenClass.UNDERSCORE;
		case '\'':
			return TokenClass.SINGLE_QUOTE;
		case '\"':
			return TokenClass.DOUBLE_QUOTE;
		case ',':
			return TokenClass.COMMA;
		case '>':
			return TokenClass.GT;
		case '<':
			return TokenClass.LT;
		default:
			return TokenClass.NONE;
		}
	}

	public Token peek() {
		// TODO Auto-generated method stub
		return null;
	}

}
