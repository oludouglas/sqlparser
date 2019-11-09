USE database1;
SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;
INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());
DELETE FROM database2.logs WHERE id < 1000;




SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;

AST: [Query(Query { ctes: [], body: Select(Select { distinct: false, projection: [UnnamedExpr(Identifier("id")), UnnamedExpr(Identifier("name")), UnnamedExpr(Identifier("address"))], from: [TableWithJoins { relation: Table { name: ObjectName(["users"]), alias: None, args: [], with_hints: [] }, joins: [] }], selection: Some(IsNotNull(Identifier("is_customer"))), group_by: [], having: None }), order_by: [OrderByExpr { expr: Identifier("created"), asc: None }], limit: None, offset: None, fetch: None })]



INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());

AST: [Insert { table_name: ObjectName(["user_notes"]), columns: ["id", "user_id", "note", "created"], source: Query { ctes: [], body: Values(Values([[Value(Number("1")), Value(Number("1")), Identifier("\"Note 1\""), Function(Function { name: ObjectName(["NOW"]), args: [], over: None, distinct: false })]])), order_by: [], limit: None, offset: None, fetch: None } }]



USE database1; 
				token		str
	-> Lexer -> USE 	| database1 | ;
	
takes USE and marks it as keyword
takes database1 and marks it as (table, database) table
	
SELECT id, name, address FROM users WHERE is_customer IS NOT NULL ORDER BY created;
				token	 str  str	str		 token  str		token		str		 token  token token	  token  token	str		  token
	-> Lexer -> SELECT | id, name, address | FROM | users | WHERE | is_customer | IS   | NOT | NULL | ORDER | BY   | created | ;
	
takes SELECT and marks it as keyword
takes id and marks it as table column name
takes name and marks it as table column name
takes address and marks it as table column name
takes FROM and marks it as keyword
takes users and marks it as (table, view, function, procedure) table
takes WHERE and marks it as keyword
takes is_customer and marks it as table column name
takes IS and marks it as keyword
takes NOT and marks it as keyword
takes NULL and marks it as constant
takes ORDER and marks it as keyword
takes BY and marks it as keyword
takes created and marks it as table column name


	
INSERT INTO user_notes (id, user_id, note, created) VALUES (1, 1, "Note 1", NOW());
				token	token	str			token	str  str	  str   str      str          token  token	 tok	str
	-> Lexer -> INSERT | INTO | user_notes | (    | id, user_id, note, created | is_customer | )   | VALUES | ( | 1, 1, "Note 1", NOW() | ) | ;
	

DELETE FROM database2.logs WHERE id < 1000;
	-> Lexer -> DELETE | FROM | FROM | database2.logs | WHERE | id | < | 1000 | ;
