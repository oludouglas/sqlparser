package org.redlamp.lex;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Tokenizer tokenizer = new Tokenizer(
//				"SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;");
//		Token next;
//		while ((next = tokenizer.next()) != null)
//			System.out.println(next);

//		System.out.println(tokenizer.next());
//		System.out.println(tokenizer.peek());
//		System.out.println(tokenizer.next());

		Parser parser = new Parser(
				"SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;");
		parser.parse();

	}

}
