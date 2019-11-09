package org.redlamp.lex;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Tokenizer tokenizer = new Tokenizer("USE database1;");
//		Token next;
//		while ((next = tokenizer.next()) != null)
//			System.out.println(next);

//		System.out.println(tokenizer.next());
//		System.out.println(tokenizer.peek());
//		System.out.println(tokenizer.next());

		Parser parser = new Parser("USE database1;");
		parser.parse();

	}

}
