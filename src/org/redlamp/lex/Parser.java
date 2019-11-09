package org.redlamp.lex;

import java.io.InputStream;

import org.redlamp.expr.BinOp;
import org.redlamp.expr.Expr;
import org.redlamp.expr.IntLiteral;
import org.redlamp.expr.Op;
import org.redlamp.expr.StrLiteral;
import org.redlamp.expr.ToStr;
import org.redlamp.lex.Token.TokenClass;

public class Parser extends ToStr {

	Token token;
	Tokenizer tokenizer;
	StringBuilder errorLog = new StringBuilder();

	public Parser(InputStream inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public Parser(String inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public void parseStmts() {
		parseStmt();
		expect(TokenClass.END);
	}

//	USE database1;
//	SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;
//	INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());
//	DELETE FROM database2.logs WHERE id < 1000;

	void parseStmt() {
		expect(TokenClass.KEYWORD); // insert | delete | use |select

		if (accept(TokenClass.KEYWORD)) { // from | into
			expect(TokenClass.KEYWORD); // move to (from | into)
			expect(TokenClass.IDENT); // user_notes | database2.logs
			if (lookAhead(1) == TokenClass.LPAR) { // insert
				parseFuncCall();
				expect(TokenClass.KEYWORD); // values
				parseFuncCall();
			} else if (lookAhead(1) == TokenClass.KEYWORD) {
				expect(TokenClass.KEYWORD);// from
				parseAssign();
			} else
				error();
		} else if (accept(TokenClass.IDENT)) {
//			INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());
			parseArgList();
			expect(TokenClass.KEYWORD); // from
			expect(TokenClass.IDENT); // table_nm
			if (lookAhead(1) == TokenClass.KEYWORD) { // where condition
				expect(TokenClass.KEYWORD); // WHERE
				parseAssign();
			}
		} else { // select dialect
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

//	SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;
	void parseAssign() {
		expect(TokenClass.IDENT);
		if (accept(TokenClass.LT)) {
			expect(TokenClass.LT);
			parseExpr();
		} else if (accept(TokenClass.LT)) {
			expect(TokenClass.LT);
			parseExpr();
		} else if (accept(TokenClass.KEYWORD)) {
			expect(TokenClass.KEYWORD); // is
			expect(TokenClass.KEYWORD); // not
			expect(TokenClass.KEYWORD); // null
			expect(TokenClass.KEYWORD); // order
			expect(TokenClass.KEYWORD); // by
			expect(TokenClass.IDENT); // created
		}
	}

	Expr parseExpr() {
		Expr lhs = parseTerm();
		if (accept(TokenClass.EQ)) {
			nextToken();
			Op op;
			switch (token.tokenClass) {
			case EQ:
				op = Op.ADD;
				break;
			default:
				op = Op.GTE;
				break;
			}
			return new BinOp(op, lhs, parseExpr());
		}
		return lhs;
	}

	Expr parseTerm() {
		Expr lhs = parseFactor();
		if (accept(TokenClass.MULT) || accept(TokenClass.DIV)) {
			Op op;
			if (token.tokenClass == TokenClass.MULT) {
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
		}
		return token;
	}

	private void error() {
		throw new IllegalArgumentException(errorLog.toString());
	}

	TokenClass lookAhead(int i) {
		return tokenizer.peek().tokenClass;
	}

}
