package org.redlamp.lex;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.redlamp.ast.BaseLiteral;
import org.redlamp.ast.Expr;
import org.redlamp.ast.Func;
import org.redlamp.ast.Identifier;
import org.redlamp.ast.Insert;
import org.redlamp.ast.IntLiteral;
import org.redlamp.ast.OrderByExpr;
import org.redlamp.ast.Select;
import org.redlamp.ast.StrLiteral;
import org.redlamp.ast.Table;
import org.redlamp.ast.Use;
import org.redlamp.lex.Token.TokenClass;

public class Parser {

	Token token;
	Tokenizer tokenizer;
	StringBuilder errorBuffer = new StringBuilder();

	public Parser(InputStream inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public Parser(String inputStream) {
		this.tokenizer = new Tokenizer(inputStream);
	}

	public void parse() {
		while (accept(TokenClass.KEYWORD)) {
			parseStatements();
		}
		System.out.println("OK");
	}

	void parseStatements() {
		parseUseStmt();
		parseSelectStmt();

		expect(TokenClass.KEYWORD); // insert | delete | use |select
		if (accept(TokenClass.KEYWORD)) { // from | into
			parseKeyWord();
			parseIdent();
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
		}
		expect(TokenClass.END);
	}

	Use parseUseStmt() {
		Use use = new Use();
		expect(TokenClass.KEYWORD);
		use.command = token.data;
		if (accept(TokenClass.IDENT)) {
			expect(TokenClass.IDENT);
			use.database = token.data;
		}
		expect(TokenClass.END);
		return use;
	}

	Insert parseInsertStmt() {
		expect(TokenClass.KEYWORD); // insert
		expect(TokenClass.KEYWORD); // into
		Expr tableName = parseIdent();
		Func tableColumns = parseFuncCall();
		tableColumns.expr = tableName;
		Expr valuesName = parseKeyWord();
		Func values = parseFuncCall(); // values list
		values.expr = valuesName;
		return new Insert(tableColumns, values);
	}

	Select parseSelectStmt() {
		expect(TokenClass.KEYWORD);
		List<Identifier> identifiers = parsArgRep();
		expect(TokenClass.KEYWORD); // from
		List<Table> relations = parseTables();
		List<Expr> conditions = parseConditions();
		OrderByExpr orderByExprs = parseOrderBy();
		return new Select(identifiers, relations, conditions, orderByExprs);
	}

	private OrderByExpr parseOrderBy() {
		// TODO Auto-generated method stub

		return null;
	}

	private List<Expr> parseConditions() {
		List<Expr> conditions = null;
		if (accept(TokenClass.KEYWORD)) { // if where clause is supported
			expect(TokenClass.KEYWORD); // where
			conditions = parseAssign();
		}
		return conditions;
	}

	private List<Table> relations = new ArrayList<Table>();;

	private List<Table> parseTables() {
		expect(TokenClass.IDENT); // MUST HAVE
		relations.add(new Table(token.data));
		if (accept(TokenClass.COMMA))
			parseTables();
		return relations;
	}

	Table parseTable() {
		expect(TokenClass.IDENT); // MUST HAVE
		return new Table(token.data);
	}

	Func parseFuncCall() {
		expect(TokenClass.LPAR);
		List<Identifier> parseArgList = parseArgList();
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	List<Identifier> parseArgList() {
		return parsArgRep();
	}

	List<Identifier> parsArgRep() {
		List<Identifier> identifiers = new ArrayList<Identifier>();
		if (accept(TokenClass.RPAR))
			return identifiers;

		if (accept(TokenClass.IDENT) || accept(TokenClass.NUMBER) || accept(TokenClass.STR)) {
			nextToken();
			identifiers.add(new Identifier(token));
		}
		if (accept(TokenClass.COMMA)) {
			nextToken();
			if (accept(TokenClass.IDENT) || accept(TokenClass.NUMBER) || accept(TokenClass.STR)
					|| accept(TokenClass.KEYWORD)) {
				nextToken();
				identifiers.add(new Identifier(token));
				parsArgRep();
			} else
				error();
		}
		return identifiers;
	}

	List<Expr> conditions = new ArrayList<Expr>();

	List<Expr> parseAssign() {
		expect(TokenClass.IDENT);
		if (accept(TokenClass.LT)) {
			expect(TokenClass.LT);
			conditions.add(parseExpr());
		}
		if (accept(TokenClass.KEYWORD)) {
			expect(TokenClass.KEYWORD);
			conditions.add(parseExpr());
		}
		return conditions;
	}

	Expr parseExpr() {
		return parseTerm();
	}

	Expr parseTerm() {
		return parseFactor();
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
		} else if (accept(TokenClass.KEYWORD)) {
			return parseKeyWord();
		}
		return parseIdent();
	}

	Expr parseKeyWord() {
		Token n = expect(TokenClass.KEYWORD);
		return new BaseLiteral(n.data);
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

	Token nextToken() {
		this.token = tokenizer.next();
		return token;
	}

	boolean accept(TokenClass ident) {
		return lookAhead(1) == ident;
	}

	Token expect(TokenClass ident) {
		nextToken();
		if (token.tokenClass != ident) {
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

	TokenClass lookAhead(int stepsAhead) {
		Token peek = tokenizer.peek();
		return peek != null ? peek.tokenClass : null;
	}

}
