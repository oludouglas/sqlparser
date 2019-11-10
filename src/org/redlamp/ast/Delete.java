package org.redlamp.ast;

import java.util.List;

public class Delete implements ASTNode {

	public List<String> columns;
	public Table table;
	public List<Expr> conditions;

	public Delete(List<String> columns, Table table, List<Expr> conditions) {
		super();
		this.columns = columns;
		this.table = table;
		this.conditions = conditions;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitDelete(this);
	}

}
