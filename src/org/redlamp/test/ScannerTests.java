package org.redlamp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.redlamp.lex.Scanner;
import org.redlamp.util.SQLUtil;

public class ScannerTests {

	public ScannerTests() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testNextWithInputStream() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Scanner scanner = new Scanner(fis);
			Assert.assertNotNull(scanner);
			Assert.assertEquals('U', scanner.next());
			Assert.assertEquals('S', scanner.next());
		}
	}

	@Test
	public void testNextWithInputString() {
		Scanner scanner = new Scanner(SQLUtil.insert);
		Assert.assertNotNull(scanner);
		Assert.assertEquals('I', scanner.next());
		Assert.assertEquals('N', scanner.next());
	}

	@Test
	public void testPeekWithInputStream() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Scanner scanner = new Scanner(fis);
			Assert.assertNotNull(scanner);
			char next = scanner.next();
			Assert.assertNotEquals("U", next);
			Assert.assertEquals('U', next);
			char peek = scanner.peek();
			Assert.assertNotNull(peek);
			Assert.assertEquals('S', peek);
			Assert.assertNotEquals("S", peek);
			Assert.assertEquals('S', scanner.next());
		}
	}

	@Test
	public void testPeekWithInputString() {
		Scanner scanner = new Scanner(SQLUtil.insert);
		Assert.assertNotNull(scanner);
		char next = scanner.next();
		Assert.assertNotEquals("I", next);
		Assert.assertEquals('I', next);
		char peek = scanner.peek();
		Assert.assertNotNull(peek);
		Assert.assertEquals('N', peek);
		Assert.assertNotEquals("N", peek);
		Assert.assertEquals('N', scanner.next());
	}

}
