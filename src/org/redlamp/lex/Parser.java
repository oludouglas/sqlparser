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
	StringBuilder errorBuffer = new StringBuilder();
	int statementCount = 0;
	Expr[] expressions;

	public Parser(InputStream inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public Parser(String inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public void parse() {
		while (accept(TokenClass.KEYWORD)) {
			parseStatement();
			expect(TokenClass.END);
			statementCount++;
		}
		System.out.println("Parsed " + statementCount);
	}

//	AST: [Insert { table_name: ObjectName(["user_notes"]), columns: ["id", "user_id", "note", "created"], source: Query { ctes: [], body: Values(Values([[Value(Number("1")), Value(Number("1")), Identifier("\"Note 1\""), Function(Function { name: ObjectName(["NOW"]), args: [], over: None, distinct: false })]])), order_by: [], limit: None, offset: None, fetch: None } }]

	void parseStatement() {
		expect(TokenClass.KEYWORD); // insert | delete | use |select
		if (accept(TokenClass.KEYWORD)) { // from | into
			expect(TokenClass.KEYWORD); // move to (from | into)
			expect(TokenClass.IDENT); // user_notes | database2.logs
			if (lookAhead(1) == TokenClass.LPAR) { // insert
				parseFuncCall();
				expect(TokenClass.KEYWORD); // values
				parseFuncCall();
			} else if (lookAhead(1) == TokenClass.KEYWORD) {
				expect(TokenClass.KEYWORD);// from || where
				parseAssign();
			} else
				error();
		} else if (accept(TokenClass.IDENT)) {
			expect(TokenClass.IDENT);
			if (accept(TokenClass.LPAR)) {
				parseArgList();
				expect(TokenClass.KEYWORD); // from
				expect(TokenClass.IDENT); // table_nm
				if (lookAhead(1) == TokenClass.KEYWORD) { // where condition
					expect(TokenClass.KEYWORD); // WHERE
					parseAssign();
				}
			} else if (lookAhead(1) == TokenClass.COMMA) { // select dialect
				parsArgRep();
				expect(TokenClass.KEYWORD); // from
				expect(TokenClass.IDENT); // table_nm
				if (accept(TokenClass.KEYWORD)) { // if where clause is supported
					expect(TokenClass.KEYWORD); // where
					parseAssign();
				}
			}
		} else {
			error();
		}
	}

	void parseFuncCall() {
		expect(TokenClass.LPAR);
		parseArgList();
		expect(TokenClass.RPAR);
	}

	void parseArgList() {
		if (accept(TokenClass.IDENT) || accept(TokenClass.NUMBER) || accept(TokenClass.STR)) {
			nextToken();
			parsArgRep();
		}
	}

	void parsArgRep() {
		if (accept(TokenClass.COMMA)) {
			nextToken();
			if (accept(TokenClass.IDENT) || accept(TokenClass.NUMBER) || accept(TokenClass.STR)
					|| accept(TokenClass.KEYWORD)) {
				nextToken();
				parsArgRep();
			} else
				error();
		}
	}

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
			return new BinOp(Op.EQ, lhs, parseExpr());
		}
		return lhs;
	}

	Expr parseTerm() {
		Expr lhs = parseFactor();
		if (accept(TokenClass.PLUS) || accept(TokenClass.MINUS)) {
			nextToken();
			Op op;
			if (token.tokenClass == TokenClass.PLUS)
				op = Op.ADD;
			else
				op = Op.SUB;
			return new BinOp(op, lhs, parseTerm());
		}
		return lhs;
	}

	Expr parseFactor() {
		if (accept(TokenClass.LPAR)) {
			Expr parseExpr = parseExpr();
			expect(TokenClass.RPAR);
			return parseExpr;
		} else if (accept(TokenClass.NUMBER)) {
			return parseNumber();
		} else if (accept(TokenClass.STR)) {
			return parseString();
		}
		return parseIdent();
	}

	Expr parseNumber() {
		Token n = expect(TokenClass.NUMBER);
		int i = Integer.parseInt(n.data);
		return new IntLiteral(i);
	}

	Expr parseString() {
		Token n = expect(TokenClass.STR);
		return new StrLiteral(n.data);
	}

	Expr parseIdent() {
		Token n = expect(TokenClass.IDENT);
		return new StrLiteral(n.data);
	}

	void nextToken() {
		token = tokenizer.next();
	}

	boolean accept(TokenClass ident) {
		return lookAhead(1) == ident;
	}

	Token expect(TokenClass ident) {
		nextToken();
		boolean expected = token.tokenClass == ident;
		if (!expected) {
			errorBuffer.setLength(0);
			errorBuffer.append("Invalid token type expected. Expecting ").append(ident).append(" but got ")
					.append(token.tokenClass);
			error();
		}
		return token;
	}

	void error() {
		throw new IllegalArgumentException(errorBuffer.toString());
	}

	TokenClass lookAhead(int i) {
		Token peek = tokenizer.peek();
		return peek != null ? peek.tokenClass : null;
	}

}
