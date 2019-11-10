package org.redlamp.ast;

public class Use implements AstNode {

	public String command;
	public String database;

	public Use() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Use(String command, String database) {
		super();
		this.command = command;
		this.database = database;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitUse(this);
	}

}
