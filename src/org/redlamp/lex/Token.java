package org.redlamp.lex;

public class Token {

	public enum TokenClass {
		IDENT, NUMBER, PLUS, MINUS, MULT, DIV, KEYWORD, GT, LT, LEQ, GEQ, EQ, SINGLE_QUOTE, DOUBLE_QUOTE, END, COMMA,
		NONE, UNDERSCORE, LPAR, RPAR, BEGIN, STR
	}

	final TokenClass tokenClass;
	final String data;
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
		return " [tokenClass:" + tokenClass + ", value: " + data + " ]";
	}

}
