package org.redlamp.lex;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.redlamp.lex.Token.TokenClass;

public class Tokenizer {

	Scanner scanner;
	boolean peeked = false;
	Token currentToken, peekedToken;
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

		keywords.put("order", TokenClass.KEYWORD);
		keywords.put("by", TokenClass.KEYWORD);

		keywords.put("now()", TokenClass.KEYWORD);
		keywords.put("is", TokenClass.KEYWORD);
	}

	public Tokenizer(String inputString) {
		scanner = new Scanner(inputString);
		initKeywords();
	}

	Token next() {
		if (peeked) {
			peeked = false;
			return peekedToken;
		}
		char next = scanner.next();
		if (Character.isWhitespace(next))
			return next();
		return buildToken(next);
	}

	Token peek() {
		peekedToken = next();
		peeked = true;
		return peekedToken;
	}

	Token buildToken(char c) {

		// operators
		TokenClass ident = identify(c);
		if (ident != TokenClass.NONE) {
			return new Token(ident, Character.toString(c));
		} else if (ident == TokenClass.END) {
			return null;
		}

		// identifier
		Token token = identifyLetter(c, c == '\'' || c == '\"');
		if (token != null)
			return token;

		// number
		token = identifyNumber(c);
		if (token != null)
			return token;

		return null;
	}

	private Token identifyNumber(char c) {
		if (Character.isDigit(c)) {
			StringBuilder builder = new StringBuilder();
			builder.append(c);
			if (scanner.peek() == ',') // used for arg parsing
				return new Token(TokenClass.NUMBER, builder.toString());
			c = scanner.next();
			while (Character.isDigit(c)) {
				builder.append(c);
				if (scanner.peek() == ',' || scanner.peek() == ';') // used for arg parsing
					break;
				c = scanner.next();
			}
			return new Token(TokenClass.NUMBER, builder.toString());
		}
		return null;
	}

	private Token identifyLetter(char c, boolean hasQuote) {
		if (Character.isLetter(c) || hasQuote) {
			StringBuilder builder = new StringBuilder();
			builder.append(c);
			c = scanner.next();
			while (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '(' || c == ')' || hasQuote) {
				builder.append(c);
				if (c == '\'' || c == '\"')
					break;
				if (scanner.peek() == ')' && c == '(') // close off functions
					builder.append(scanner.next());
				if (scanner.peek() == ')' && c != ')')
					break;
				if (scanner.peek() == ';')
					break;
				if (scanner.peek() == ',')
					break;
				c = scanner.next();
			}
			String stringLiteral = builder.toString();
			if (keywords.containsKey(stringLiteral.toLowerCase()))
				return new Token(TokenClass.KEYWORD, stringLiteral);
			if ((stringLiteral.startsWith("\"") || stringLiteral.startsWith("\'")))
				return new Token(TokenClass.STR, stringLiteral);
			return new Token(TokenClass.IDENT, stringLiteral);
		}
		return null;
	}

	private TokenClass identify(char c) {
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
			return TokenClass.RPAR;
		case '(':
			return TokenClass.LPAR;
		case ';':
			return TokenClass.END;
		case '_':
			return TokenClass.UNDERSCORE;
		case ',':
			return TokenClass.COMMA;
		case '>':
			return TokenClass.GT;
		case '<':
			return TokenClass.LT;
		case ' ':
			return TokenClass.NONE;
		case '\t':
			return TokenClass.NONE;
		default:
			return TokenClass.NONE;
		}
	}

}
