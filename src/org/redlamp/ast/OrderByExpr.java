package org.redlamp.ast;

public class OrderByExpr implements ASTNode {

	public Identifier expr;
	public String asc;

	public OrderByExpr(Identifier expr, String asc) {
		super();
		this.expr = expr;
		this.asc = asc;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitOrderByExpr(this);
	}

}
