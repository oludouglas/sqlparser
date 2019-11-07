package org.redlamp.expr;

public class ToStr implements Visitor<String> {

	@Override
	public String visitIntLiteral(IntLiteral i) {
		// TODO Auto-generated method stub
		return "" + i.i;
	}

	@Override
	public String visitBiOp(BinOp bo) {
		// TODO Auto-generated method stub
		return bo.lhs.accept(this) + bo.op.name() + bo.rhs.accept(this);
	}

}
