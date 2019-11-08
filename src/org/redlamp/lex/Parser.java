package org.redlamp.lex;

import java.io.InputStream;

import org.redlamp.expr.BinOp;
import org.redlamp.expr.Expr;
import org.redlamp.expr.IntLiteral;
import org.redlamp.expr.Op;
import org.redlamp.expr.StrLiteral;
import org.redlamp.lex.Token.TokenClass;

public class Parser {

	Token token;
	Tokenizer tokenizer;
	StringBuilder errorLog = new StringBuilder();

	public Parser(InputStream inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	void parseStmts() {
		nextToken();
		parseStmt();
		expect(TokenClass.END);
	}

//	USE database1;
//	SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;
//	INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());
//	DELETE FROM database2.logs WHERE id < 1000;

	void parseStmt() {
		expect(TokenClass.KEYWORD); // insert | delete | use |select
		if (lookAhead(1) == TokenClass.KEYWORD) { // from | into
			expect(TokenClass.KEYWORD); // move to (from | into)
			expect(TokenClass.IDENT); // user_notes | database2.logs
			if (lookAhead(1) == TokenClass.LPAR) {
				parseFuncCall();
				expect(TokenClass.KEYWORD); // values
				parseFuncCall();
			} else if (lookAhead(1) == TokenClass.KEYWORD) {
				expect(TokenClass.KEYWORD);
				parseAssign();
			} else
				error();
		} else { // variable
			error();
		}
	}

//	INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());
	void parseFuncCall() {
//		expect(TokenClass.IDENT);
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

	void parseAssign() {
		expect(TokenClass.IDENT);
//		expect(TokenClass.LT);
		parseExpr();
	}

	Expr parseExpr() {
		Expr lhs = parseTerm();
		if (accept(TokenClass.LT)) {
			nextToken();
			Op op;
			if (token.tokenClass == TokenClass.LT) {
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

	Expr parseIdent() {
		Token n = expect(TokenClass.IDENT);
		return new StrLiteral(n.data);
	}

	void nextToken() {
		token = tokenizer.next();
	}

	private boolean accept(TokenClass ident) {
		boolean accepted = lookAhead(1) == ident;
		if (!accepted) {
			errorLog.setLength(0);
			errorLog.append("Invalid token type accepted. Expecting ").append(ident).append(" but got ")
					.append(token.tokenClass);
		}
		return accepted;
	}

	Token expect(TokenClass ident) {
		nextToken();
		boolean expected = token.tokenClass == ident;
		if (!expected) {
			errorLog.setLength(0);
			errorLog.append("Invalid token type expected. Expecting ").append(ident).append(" but got ")
					.append(token.tokenClass);
			error();
			return null;
		}
		return token;
	}

	private void error() {
		System.err.println(errorLog.toString());
		errorLog.setLength(0);
	}

	TokenClass lookAhead(int i) {
		return tokenizer.peek().tokenClass;
	}

}
