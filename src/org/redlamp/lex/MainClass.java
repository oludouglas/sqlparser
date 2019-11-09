package org.redlamp.lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		Tokenizer tokenizer = new Tokenizer(
//				"SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;");
//		Token next;
//		while ((next = tokenizer.next()) != null)
//			System.out.println(next);

		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Parser parser = new Parser(fis);
			parser.parse();
		}

	}

}
