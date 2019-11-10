package org.redlamp.lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.redlamp.ast.Statement;
import org.redlamp.expr.ASTPrinter;

public class MainClass {

	static String use = "USE database1;";
	static String select = "SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;";
	static String insert = "INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());";
	static String delete = "DELETE FROM database2.logs WHERE id < 1000;";

	public static void main(String[] args) throws IOException {

//		Tokenizer tokenizer = new Tokenizer(select);
//		Token token;
//		while ((token = tokenizer.next()) != null)
//			System.out.println(token);

//		Parser parser = new Parser(delete);
//		Delete parseUseStmt = parser.parseStatements();
//
//		PrintWriter writer;
//		try (StringWriter sw = new StringWriter();) {
//			writer = new PrintWriter(sw);
//			parseUseStmt.accept(new ASTPrinter(writer));
//			writer.flush();
//			System.out.print(sw.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {

			Tokenizer tokenizer = new Tokenizer(fis);
			Token token;
			while ((token = tokenizer.next()) != null) {
				System.out.println(token);
				System.out.println("============================================");
			}

			Parser parser = new Parser(fis);
			Statement statement = parser.parseStatements();

			PrintWriter writer;
			StringWriter sw = new StringWriter();
			try {
				writer = new PrintWriter(sw);
				statement.accept(new ASTPrinter(writer));
				writer.flush();
				System.out.print(sw.toString());
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
