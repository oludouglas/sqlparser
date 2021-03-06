package org.redlamp.ast;

import java.util.List;

public class Delete implements AstNode {

	public List<Identifier> columns;
	public Table table;
	public WhereClause conditions;

	public Delete(List<Identifier> columns, Table table, WhereClause conditions) {
		super();
		this.columns = columns;
		this.table = table;
		this.conditions = conditions;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitDelete(this);
	}

}
