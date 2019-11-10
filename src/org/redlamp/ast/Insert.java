package org.redlamp.ast;

public class Insert implements ASTNode {

	public Expr relation;
	public Func columns;
	public Func values;

	public Insert(Func columns, Func values) {
		super();
		this.relation = columns.expr;
		this.columns = columns;
		this.values = values;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitInsert(this);
	}

}
