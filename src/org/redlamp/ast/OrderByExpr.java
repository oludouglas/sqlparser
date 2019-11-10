package org.redlamp.ast;

import java.util.List;

public class OrderByExpr implements AstNode {

	public List<Identifier> expr;
	public boolean desc;

	public OrderByExpr(List<Identifier> expr, boolean desc) {
		super();
		this.expr = expr;
		this.desc = desc;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitOrderByExpr(this);
	}

}
