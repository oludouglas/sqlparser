package org.redlamp.ast;

public class Table implements AstNode {

	public final String relation;

	public Table(String relation) {
		super();
		this.relation = relation;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitTable(this);
	}

}
