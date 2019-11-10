package org.redlamp.lex;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.redlamp.ast.Select;
import org.redlamp.expr.ASTPrinter;

public class MainClass {

	public static void main(String[] args) throws IOException {

		Parser parser = new Parser("SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;");
		Select parseUseStmt = parser.parseSelectStmt();

		PrintWriter writer;
		StringWriter sw = new StringWriter();
		try {
			writer = new PrintWriter(sw);
			parseUseStmt.accept(new ASTPrinter(writer));
			writer.flush();
			System.out.print(sw.toString());
			writer.close();
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
