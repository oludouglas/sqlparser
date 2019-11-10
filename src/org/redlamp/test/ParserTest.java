package org.redlamp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.redlamp.ast.Delete;
import org.redlamp.ast.Insert;
import org.redlamp.ast.Select;
import org.redlamp.ast.Statement;
import org.redlamp.ast.Use;
import org.redlamp.lex.Parser;
import org.redlamp.util.SQLUtil;

class ParserTest {

	@Test
	void testParseStatements() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Parser parser = new Parser(fis);
			Assert.assertNotNull(parser);

			Statement statement = parser.parseStatements();
			Assert.assertNotNull(statement);

			String ast = SQLUtil.buildTree(statement);
			Assert.assertNotNull(ast);
		}
	}

	@Test
	void testAutoParse() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Parser parser = new Parser(fis);
			Assert.assertNotNull(parser);
			parser.autoParse();
		}
	}

	@Test
	void testParseUseStmt() {
		Parser parser = new Parser(SQLUtil.use);
		Assert.assertNotNull(parser);

		Use statement = parser.parseUseStmt();
		Assert.assertNotNull(statement);

		Assert.assertEquals("USE", statement.command);
		Assert.assertEquals("database1", statement.database);

		String ast = SQLUtil.buildTree(statement);
		Assert.assertNotNull(ast);
	}

	@Test
	void testParseInsertStmt() {
		Parser parser = new Parser(SQLUtil.insert);
		Assert.assertNotNull(parser);

		Insert statement = parser.parseInsertStmt();
		Assert.assertNotNull(statement);

		Assert.assertNotNull(statement.columns);
		Assert.assertNotNull(statement.relation);
		Assert.assertNotNull(statement.values);

		String ast = SQLUtil.buildTree(statement);
		Assert.assertNotNull(ast);
	}

	@Test
	void testParseSelectStmt() {
		Parser parser = new Parser(SQLUtil.select);
		Assert.assertNotNull(parser);

		Select statement = parser.parseSelectStmt();
		Assert.assertNotNull(statement);

		Assert.assertNotNull(statement.identifiers);
		Assert.assertNotNull(statement.conditions);
		Assert.assertNotNull(statement.relations);
		Assert.assertNotNull(statement.orderByExprs);

		String ast = SQLUtil.buildTree(statement);
		Assert.assertNotNull(ast);
	}

	@Test
	void testParseDeleteStmt() {
		Parser parser = new Parser(SQLUtil.delete);
		Assert.assertNotNull(parser);

		Delete statement = parser.parseDeleteStmt();
		Assert.assertNotNull(statement);

		Assert.assertNotNull(statement.columns);
		Assert.assertNotNull(statement.conditions);
		Assert.assertNotNull(statement.table);

		String ast = SQLUtil.buildTree(statement);
		Assert.assertNotNull(ast);
	}

}
