package org.redlamp.rec;

public class Runner {

	public static void main(String[] args) {
		try {
			Parser.STATEMENT_LIST
					.parse("SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;");
			System.out.println("OK");
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

}
