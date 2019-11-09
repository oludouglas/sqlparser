package org.redlamp.lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {

		try (FileInputStream fis = new FileInputStream(new File("simple-sql-parser-short/operations.sql"))) {
			Parser parser = new Parser(fis);
			parser.parse();
		}

	}

}
