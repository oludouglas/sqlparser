package org.redlamp.lex;

public class Token {

	public enum TokenClass {
		IDENT, NUMBER, PLUS, MINUS, MULT, DIV, KEYWORD, GT, LT, LEQ, GTE, EQ, SINGLE_QUOTE, DOUBLE_QUOTE, END, COMMA,
		NONE, UNDERSCORE, LPAR, RPAR, BEGIN, STR
	}

	public final TokenClass tokenClass;
	public final String data;
	final int pos;

	public Token(TokenClass tokenClass) {
		super();
		this.tokenClass = tokenClass;
		this.data = null;
		this.pos = 0;
	}

	public Token(TokenClass tokenClass, String data) {
		super();
		this.tokenClass = tokenClass;
		this.data = data;
		this.pos = 0;
	}

	public Token(TokenClass tokenClass, String data, int pos) {
		super();
		this.tokenClass = tokenClass;
		this.data = data;
		this.pos = pos;
	}

	@Override
	public String toString() {
		if (data.equals(""))
			return tokenClass.toString();
		else
			return tokenClass.toString() + "(" + data + ")";
	}

}
