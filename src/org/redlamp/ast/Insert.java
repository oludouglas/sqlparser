package org.redlamp.ast;

import java.util.List;

public class Insert implements ASTNode {

	public Table relation;
	public List<Expr> columns;
	public List<Expr> values;

	public Insert(Table relation, List<Expr> columns, List<Expr> values) {
		super();
		this.relation = relation;
		this.columns = columns;
		this.values = values;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitInsert(this);
	}

}
