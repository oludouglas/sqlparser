package org.redlamp.ast;

public class Table implements ASTNode {

	public final String relation;

	public Table(String relation) {
		super();
		this.relation = relation;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitTable(this);
	}

}
