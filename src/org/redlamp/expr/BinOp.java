package org.redlamp.expr;

import org.redlamp.ast.AstVisitor;
import org.redlamp.ast.BaseType;
import org.redlamp.ast.Expr;

public class BinOp extends Expr {

	public Op op;
	public Expr lhs;
	public Expr rhs;

	public BinOp(Op op, Expr lhs, Expr rhs) {
		super();
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitBaseType(BaseType.OPERATOR);
	}

}
