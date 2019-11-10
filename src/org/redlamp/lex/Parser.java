package org.redlamp.lex;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.redlamp.ast.BaseLiteral;
import org.redlamp.ast.Delete;
import org.redlamp.ast.Expr;
import org.redlamp.ast.Func;
import org.redlamp.ast.Identifier;
import org.redlamp.ast.Insert;
import org.redlamp.ast.IntLiteral;
import org.redlamp.ast.OrderByExpr;
import org.redlamp.ast.Select;
import org.redlamp.ast.Statement;
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
		parseStatements();
	}

	public Statement parseStatements() {
		Use use = parseUseStmt();
		Select select = parseSelectStmt();
		Insert insert = parseInsertStmt();
		Delete delete = parseDeleteStmt();
		return new Statement(use, select, insert, delete);
	}

	public void autoParse() {
		while (accept(TokenClass.KEYWORD)) {
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
	}

	public Use parseUseStmt() {
		Use use = new Use();
		expect(TokenClass.KEYWORD);
		use.command = token.data;
		if (accept(TokenClass.IDENT)) {
			expect(TokenClass.IDENT);
			use.database = token.data;
		}
		return use;
	}

	public Insert parseInsertStmt() {
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
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	Func parseKeyFuncCall() {
		expect(TokenClass.LPAR);
		List<Identifier> parseArgList = parseArgList();
		expect(TokenClass.RPAR);
		return new Func(null, parseArgList);
	}

	public Select parseSelectStmt() {
		expect(TokenClass.KEYWORD);
		List<Identifier> identifiers = parseSelectArgList();
		expect(TokenClass.KEYWORD); // from
		List<Table> relations = parseTables();
		WhereClause conditions = parseWhereExpr();
		OrderByExpr orderByExprs = parseOrderBy();
		return new Select(identifiers, relations, conditions, orderByExprs);
	}

	public Delete parseDeleteStmt() {
		expect(TokenClass.KEYWORD);
		List<Identifier> columns = parseSelectArgList();
		expect(TokenClass.KEYWORD); // from
		Table relation = parseTable();
		WhereClause conditions = parseWhereExpr();
		return new Delete(columns, relation, conditions);
	}

	private OrderByExpr parseOrderBy() {
		boolean asc = false;
		List<Identifier> conditionList = new ArrayList<Identifier>();
		if (accept(TokenClass.KEYWORD)) {
			nextToken();
			nextToken();
			conditionList.add(parseIdentifier());
			if (accept(TokenClass.KEYWORD)) {
				nextToken();
				asc = "ASC".equalsIgnoreCase(parseIdentifier().identifierName);
			}
		}
		return new OrderByExpr(conditionList, asc);
	}

	private WhereClause parseWhereExpr() {
		List<Identifier> conditionList = new ArrayList<Identifier>();
		if (accept(TokenClass.KEYWORD)) {
			nextToken();// skip the where keyword
			conditionList.add(parseIdentifier()); // column
			if (accept(TokenClass.KEYWORD)) {
				conditionList.add(parseKeyIdentifier());
				conditionList.add(parseKeyIdentifier());
				conditionList.add(parseKeyIdentifier());
			}
			if (accept(TokenClass.LT)) {
				conditionList.add(parseIdentFactor());
				conditionList.add(parseIdentFactor());
			}
		}
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

	Identifier parseIdentFactor() {
		if (accept(TokenClass.NUMBER)) {
			return parseNumberIdent();
		} else if (accept(TokenClass.STR)) {
			return parseStringIdent();
		} else if (accept(TokenClass.KEYWORD)) {
			return parseKeyIdentifier();
		} else if (accept(TokenClass.LT)) {
			return parseLtOp();
		} else if (accept(TokenClass.GT)) {
			return parseGtOp();
		}
		return parseIdentifier();
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

	Identifier parseNumberIdent() {
		Token n = expect(TokenClass.NUMBER);
		return new Identifier(n);
	}

	Identifier parseStringIdent() {
		Token n = expect(TokenClass.STR);
		return new Identifier(n);
	}

	Identifier parseLtOp() {
		Token n = expect(TokenClass.LT);
		return new Identifier(n);
	}

	Identifier parseGtOp() {
		Token n = expect(TokenClass.GT);
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
		if (token == null)
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
