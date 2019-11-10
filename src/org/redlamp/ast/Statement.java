package org.redlamp.ast;

public class Statement implements AstNode {

	public Use use;
	public Select select;
	public Insert insert;
	public Delete delete;

	public Statement(Use use, Select select, Insert insert, Delete delete) {
		super();
		this.use = use;
		this.select = select;
		this.insert = insert;
		this.delete = delete;
	}

	public <T> T accept(AstVisitor<T> v) {
		return v.visitStatement(this);
	}
}
