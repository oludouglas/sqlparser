package org.redlamp.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.redlamp.ast.AstNode;
import org.redlamp.expr.AstDump;

public class SQLUtil {

	public static String use = "USE database1;";
	public static String select = "SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;";
	public static String insert = "INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, \"Note 1\", NOW());";
	public static String delete = "DELETE FROM database2.logs WHERE id < 1000;";

	public static synchronized String buildTree(AstNode astNode) {
		StringWriter sw = new StringWriter();
		try (PrintWriter writer = new PrintWriter(sw)) {
			astNode.accept(new AstDump(writer));
			writer.flush();
			writer.close();
		}
		return sw.toString();
	}
}
