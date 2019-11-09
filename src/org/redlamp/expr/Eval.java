package org.redlamp.expr;

public class Eval implements Visitor<String> {

	@Override
	public String visitIntLiteral(IntLiteral i) {
		// TODO Auto-generated method stub
		return "" + i.number;
	}

	@Override
	public String visitBiOp(BinOp bo) {
		switch (bo.op) {
		case ADD:
			return "" + Integer.parseInt(bo.lhs.accept(this)) + Integer.parseInt(bo.rhs.accept(this));
		case SUB:
			return "" + (Integer.parseInt(bo.lhs.accept(this)) - Integer.parseInt(bo.rhs.accept(this)));
		case MUL:
			return "" + Integer.parseInt(bo.lhs.accept(this)) * Integer.parseInt(bo.rhs.accept(this));
		case DIV:
			return "" + Integer.parseInt(bo.lhs.accept(this)) / Integer.parseInt(bo.rhs.accept(this));
		default:
			return "0";
		}
	}

	@Override
	public String visitStrLiteral(StrLiteral strLiteral) {
		return strLiteral.name;
	}

}
