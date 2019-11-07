package org.redlamp.expr;

public class Eval implements Visitor<Integer> {

	@Override
	public Integer visitIntLiteral(IntLiteral i) {
		// TODO Auto-generated method stub
		return i.i;
	}

	@Override
	public Integer visitBiOp(BinOp bo) {

		switch (bo.op) {
		case ADD:
			return bo.lhs.accept(this) + bo.rhs.accept(this);
		case SUB:
			return bo.lhs.accept(this) - bo.rhs.accept(this);
		case MUL:
			return bo.lhs.accept(this) * bo.rhs.accept(this);
		case DIV:
			return bo.lhs.accept(this) / bo.rhs.accept(this);
		default:
			return 0;
		}
	}

}
