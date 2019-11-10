package org.redlamp.lex;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.redlamp.ast.Insert;
import org.redlamp.expr.ASTPrinter;

public class MainClass {

	static String use = "USE database1;";
	static String select = "SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;";
	static String insert = "INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());";
	static String delete = "DELETE FROM database2.logs WHERE id < 1000;";

	public static void main(String[] args) throws IOException {

		Parser parser = new Parser(insert);
		Insert parseUseStmt = parser.parseInsertStmt();

		PrintWriter writer;
		try (StringWriter sw = new StringWriter();) {
			writer = new PrintWriter(sw);
			parseUseStmt.accept(new ASTPrinter(writer));
			writer.flush();
			System.out.print(sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

//		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
//			Parser parser = new Parser(fis);
//			Select parseUseStmt = parser.parseSelectStmt();
//
//			PrintWriter writer;
//			StringWriter sw = new StringWriter();
//			try {
//				writer = new PrintWriter(sw);
//				parseUseStmt.accept(new ASTPrinter(writer));
//				writer.flush();
//				System.out.print(sw.toString());
//				writer.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}

	}

}
