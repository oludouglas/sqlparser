package org.redlamp.lex;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Tokenizer tokenizer = new Tokenizer(
//				"INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());");
//		Token next;
//		while ((next = tokenizer.next()) != null)
//			System.out.println(next);

		Parser parser = new Parser(
				"INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());");
		parser.parseStmts();

	}

}
