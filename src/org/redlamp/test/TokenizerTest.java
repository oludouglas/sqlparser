package org.redlamp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.redlamp.lex.Token;
import org.redlamp.lex.Tokenizer;
import org.redlamp.util.SQLUtil;

class TokenizerTest {

	@Test
	void testTokenizerInputStream() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Tokenizer tokenizer = new Tokenizer(fis);
			Token token = tokenizer.next();
			Assert.assertNotNull(token);
		}
	}

	@Test
	void testTokenizerString() {
		Tokenizer tokenizer = new Tokenizer(SQLUtil.delete);
		Token token = tokenizer.next();
		Assert.assertNotNull(token);
		Assert.assertEquals("DELETE", token.data);
	}

	@Test
	void testNext() {
		Tokenizer tokenizer = new Tokenizer(SQLUtil.delete);
		Token token;
		while ((token = tokenizer.next()) != null)
			Assert.assertNotNull(token);
	}

	@Test
	void testPeek() {
		Tokenizer tokenizer = new Tokenizer(SQLUtil.select);
		Token token = tokenizer.next();
		Assert.assertNotNull(token);
		Assert.assertEquals("SELECT", token.data);
		Token peek = tokenizer.peek();
		Assert.assertNotNull(peek);
		Assert.assertEquals("id", peek.data);
		Assert.assertEquals("id", tokenizer.next().data);
	}

}
