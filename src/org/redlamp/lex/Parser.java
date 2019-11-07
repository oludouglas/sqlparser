package org.redlamp.lex;

import java.io.InputStream;

import org.redlamp.expr.BinOp;
import org.redlamp.expr.Expr;
import org.redlamp.expr.IntLiteral;
import org.redlamp.expr.Op;
import org.redlamp.lex.Token.TokenClass;
import org.redlamp.rec.ParseException;

public class Parser {

	Token token;
	Tokenizer tokenizer;

	public Parser(InputStream inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	void parseStmts() {
		parseStmt();
		expect(TokenClass.END);
	}

	void parseStmt() {
		if (accept(TokenClass.IDENT)) {
			if (lookAhead(1) == TokenClass.LPAR)
				parseFuncCall();
			else if (lookAhead(1) == TokenClass.EQ) {
				parseAssign();
			} else
				error();
		} else
			error();
	}

	void parseAssign() {
		expect(TokenClass.IDENT);
		expect(TokenClass.EQ);
		parseExpr();
	}

	Expr parseExpr() {
		Expr lhs = parseTerm();
		if (accept(TokenClass.PLUS | TokenClass.MINUS)) {
			Op op;
			if (token.tokenClass == TokenClass.PLUS) {
				op = Op.ADD;
			} else {
				op = Op.SUB;
			}
			return new BinOp(op, lhs, parseExpr());
		}
		return lhs;
	}

	Expr parseTerm() {
		Expr lhs = parseFactor();
		if (accept(TokenClass.MULT | TokenClass.DIV)) {
			Op op;
			if (token == TokenClass.TIMES) {
				op = Op.MUL;
			} else {
				op = Op.DIV;
			}
			nextToken();
			return new BinOp(op, lhs, parseTerm());
		}
		return lhs;
	}
	
	void parseFuncCall() {
		expect(TokenClass.IDENT);
		expect(TokenClass.LPAR);
		parseArgList();
		expect(TokenClass.RPAR);
	}

	void parseArgList() {
		if (accept(TokenClass.IDENT)) {
			nextToken();
			parsArgRep();
		}
	}

	void parsArgRep() {
		if (accept(TokenClass.COMMA)) {
			nextToken();
			expect(TokenClass.IDENT);
			parsArgRep();
		}
	}

	private boolean accept(TokenClass ident) {
		// TODO Auto-generated method stub
		return false;
	}

	Expr parseFactor() {
		if (accept(TokenClass.LPAR)) {
			Expr parseExpr = parseExpr();
			expect(TokenClass.RPAR);
			return parseExpr;
		} else {
			return parseNumber();
		}
	}

	Expr parseNumber() {
		Token n = expect(TokenClass.NUMBER);
		int i = Integer.parseInt(n.data);
		return new IntLiteral(i);
	}

	void nextToken() {
		token = tokenizer.next();
	}

	Token expect(TokenClass ident) {

//		s = s.toLowerCase();
//		if (s.startsWith("use")) {
//			VARIABLE.parse(s.substring(3));
//		} else if (s.startsWith("select")) {
//			EXPRESSION.parse(s.substring(6));
//		} else if (s.startsWith("insert")) {
//			WHILE.parse(s.substring(5));
//		} else if (s.startsWith("delete")) {
//			WHILE.parse(s.substring(5));
//		} else if (s.contains("=")) {
//			ASSIGNMENT.parse(s);
//		} else {
//			throw new ParseException("Illegal statement: " + s);
//		}

		return null;
	}

	private void error() {
		// TODO Auto-generated method stub

	}

	Token lookAhead(int i) {
		return tokenizer.peek();
	}

}
