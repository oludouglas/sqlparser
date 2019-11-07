package org.redlamp.rec;

public enum Parser {
	PROGRAM {
		void parse(String s) throws ParseException {
			s = s.trim();
			if (s.startsWith("begin") && s.endsWith("end")) {
				STATEMENT_LIST.parse(s.substring(5, s.length() - 3));
			} else {
				throw new ParseException("Illegal begin/end");
			}
		}
	},

	STATEMENT_LIST {
		void parse(String s) throws ParseException {
			String[] parts = s.trim().split(";");
			for (String part : parts) {
				STATEMENT.parse(part.trim());
			}
		}
	},

	STATEMENT {
		void parse(String s) throws ParseException {
			s = s.toLowerCase();
			if (s.startsWith("use")) {
				VARIABLE.parse(s.substring(3));
			} else if (s.startsWith("select")) {
				EXPRESSION.parse(s.substring(6));
			} else if (s.startsWith("insert")) {
				WHILE.parse(s.substring(5));
			} else if (s.startsWith("delete")) {
				WHILE.parse(s.substring(5));
			} else if (s.contains("=")) {
				ASSIGNMENT.parse(s);
			} else {
				throw new ParseException("Illegal statement: " + s);
			}
		}
	},
	USE {
		void parse(String s) throws ParseException {
			int i = s.indexOf("(");
			int j = s.indexOf(")");
			if (i != -1 && j != -1) {
				LOGICAL.parse(s.substring(i + 1, j).trim());
				STATEMENT.parse(s.substring(j + 1).trim());
			} else {
				throw new ParseException("Illegal while: " + s);
			}
		}
	},

	SELECT {
		void parse(String s) throws ParseException {
			int i = s.indexOf("(");
			int j = s.indexOf(")");
			if (i != -1 && j != -1) {
				LOGICAL.parse(s.substring(i + 1, j).trim());
				STATEMENT.parse(s.substring(j + 1).trim());
			} else {
				throw new ParseException("Illegal while: " + s);
			}
		}
	},

	WHILE {
		void parse(String s) throws ParseException {
			int i = s.indexOf("(");
			int j = s.indexOf(")");
			if (i != -1 && j != -1) {
				LOGICAL.parse(s.substring(i + 1, j).trim());
				STATEMENT.parse(s.substring(j + 1).trim());
			} else {
				throw new ParseException("Illegal while: " + s);
			}
		}
	},

	ASSIGNMENT {
		void parse(String s) throws ParseException {
			int i = s.indexOf("=");
			if (i != -1) {
				VARIABLE.parse(s.substring(0, i).trim());
				EXPRESSION.parse(s.substring(i + 1).trim());
			}
		}
	},

	EXPRESSION {
		void parse(String s) throws ParseException {
			String[] parts = s.split("\\+|-");
			for (String part : parts) {
				VARIABLE.parse(part.trim());
			}
		}
	},

	LOGICAL {
		void parse(String s) throws ParseException {
			int i;
			if (s.contains("<")) {
				i = s.indexOf("<");
			} else if (s.contains(">")) {
				i = s.indexOf(">");
			} else {
				throw new ParseException("Illegal logical: " + s);
			}

			VARIABLE.parse(s.substring(0, i).trim());
			VARIABLE.parse(s.substring(i + 1).trim());
		}
	},

	VARIABLE {
		void parse(String s) throws ParseException {
			System.out.println(s);
			if (!s.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
				throw new ParseException("Illegal variable: " + s);
			}
		}
	};

	abstract void parse(String s) throws ParseException;

}
