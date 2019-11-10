package org.redlamp.ast;

import java.util.List;

public class WhereClause implements AstNode {

	public List<Identifier> expr;

	public WhereClause(List<Identifier> expr) {
		super();
		this.expr = expr;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitWhereClause(this);
	}

}
