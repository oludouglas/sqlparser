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
import org.redlamp.ast.WhereClause;
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
				parseArgRep();
				expect(TokenClass.KEYWORD); // from
				expect(TokenClass.IDENT); // table_nm
				if (accept(TokenClass.KEYWORD)) { // if where clause is supported
					expect(TokenClass.KEYWORD); // where
					parseAssign();
				}
			}
		}
	}

	Use parseUseStmt() {
		Use use = new Use();
		expect(TokenClass.KEYWORD);
		use.command = token.data;
		if (accept(TokenClass.IDENT)) {
			expect(TokenClass.IDENT);
			use.database = token.data;
		}
		return use;
	}

	Insert parseInsertStmt() {
		expect(TokenClass.KEYWORD); // insert
		expect(TokenClass.KEYWORD); // into

		Table parseTable = parseTable();
		Func columns = parseVarFuncCall();
		columns.expr = new StrLiteral(parseTable.relation);

		Expr value = parseKeyWord();
		Func values = parseKeyFuncCall(); // values list
		values.expr = value;

		return new Insert(parseTable, columns, values);
	}

	Func parseVarFuncCall() {
		expect(TokenClass.LPAR);
		List<Identifier> parseArgList = parseArgList();
		System.out.println("parseVarFuncCall");

		for (Identifier identifier : parseArgList)
			System.out.println(identifier.identifierName + ": " + identifier.identifierType);
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	Func parseKeyFuncCall() {
		expect(TokenClass.LPAR);
		List<Identifier> parseArgList = parseArgList();
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	Select parseSelectStmt() {
		expect(TokenClass.KEYWORD);
		List<Identifier> identifiers = parseSelectArgList();
		expect(TokenClass.KEYWORD); // from
		List<Table> relations = parseTables();
		WhereClause conditions = parseConditions();

		OrderByExpr orderByExprs = parseOrderBy();
		return new Select(identifiers, relations, conditions, orderByExprs);
	}

	private OrderByExpr parseOrderBy() {
		// TODO Auto-generated method stub

		return null;
	}

	private WhereClause parseConditions() {
		conditions.clear();
		List<Expr> conditionList = new ArrayList<Expr>();
		while (accept(TokenClass.KEYWORD))
			parseAssign();

		conditionList.addAll(conditions);
		conditions.clear();
		return new WhereClause(conditionList);
	}

	void parseKeywordAssign() {
		if (accept(TokenClass.KEYWORD))
			identifiers.add(parseKeyIdentifier());
	}

	List<Expr> parseAssign() {
		if (accept(TokenClass.IDENT))
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

	private List<Table> relations = new ArrayList<Table>();;

	private List<Table> parseTables() {
		expect(TokenClass.IDENT); // MUST HAVE
		relations.add(new Table(token.data));
		if (accept(TokenClass.COMMA))
			parseTables();

		List<Table> tables = new ArrayList<Table>(relations);
		relations.clear();
		return tables;
	}

	Table parseTable() {
		expect(TokenClass.IDENT); // MUST HAVE
		return new Table(token.data);
	}

	Func parseFuncCall() {
		expect(TokenClass.LPAR);
		Identifier parseIdent = parseIdentifier();
		List<Identifier> parseArgList = parseArgList();
		parseArgList.add(0, parseIdent);

		for (Identifier identifier : parseArgList)
			System.out.println(identifier.identifierName + ": " + identifier.identifierType);
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	List<Identifier> parseArgList() {
		identifiers.clear();
		while (!accept(TokenClass.RPAR)) {
			parseArgRep();
		}
		List<Identifier> args = new ArrayList<Identifier>(identifiers);
		identifiers.clear();
		return args;
	}

	List<Identifier> parseSelectArgList() {
		identifiers.clear();
		while (!accept(TokenClass.KEYWORD)) {
			parseArgRep();
		}
		List<Identifier> args = new ArrayList<Identifier>(identifiers);
		identifiers.clear();
		return args;
	}

	List<Identifier> identifiers = new ArrayList<Identifier>();

	List<Identifier> parseArgRep() {
		if (accept(TokenClass.IDENT) || accept(TokenClass.NUMBER) || accept(TokenClass.STR)
				|| accept(TokenClass.KEYWORD)) {
			nextToken();
			identifiers.add(new Identifier(token));
			if (accept(TokenClass.COMMA)) {
				nextToken();// skip commas
			}
		}
		return identifiers;
	}

	List<Expr> conditions = new ArrayList<Expr>();

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

	Identifier parseIdentifier() {
		Token n = expect(TokenClass.IDENT);
		return new Identifier(n);
	}

	Identifier parseKeyIdentifier() {
		Token n = expect(TokenClass.KEYWORD);
		return new Identifier(n);
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
