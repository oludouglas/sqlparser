package org.redlamp.expr;

import org.redlamp.syn.Type;

public class BinOp extends Expr {

	public Op op;
	public Expr lhs;
	public Expr rhs;
	public Type type;

	public BinOp(Op op, Expr lhs, Expr rhs) {
		super();
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public String toStr() {
		return lhs.toStr() + op.name() + rhs.toStr();
	}

	@Override
	public int eval() {
		switch (op) {
		case ADD:
			return lhs.eval() + rhs.eval();
		case SUB:
			return lhs.eval() - rhs.eval();
		case MUL:
			return lhs.eval() * rhs.eval();
		case DIV:
			return lhs.eval() / rhs.eval();
		default:
			return 0;
		}
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitBiOp(this);
	}

}
