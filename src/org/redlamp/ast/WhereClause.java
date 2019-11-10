package org.redlamp.ast;

import java.util.List;

public class WhereClause implements ASTNode {

	public List<Expr> expr;

	public WhereClause(List<Expr> expr) {
		super();
		this.expr = expr;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitWhereClause(this);
	}

}
