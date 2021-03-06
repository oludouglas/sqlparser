package org.redlamp.ast;

public class Insert implements AstNode {

	public Table relation;
	public Func columns;
	public Func values;

	public Insert(Table relation, Func columns, Func values) {
		super();
		this.relation = relation;
		this.columns = columns;
		this.values = values;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitInsert(this);
	}

}
