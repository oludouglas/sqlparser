package org.redlamp.expr;

public class IntLiteral extends Expr {

	int number;

	public IntLiteral(int i) {
		super();
		this.number = i;
	}

	@Override
	public String toStr() {
		// TODO Auto-generated method stub
		return "" + number;
	}

	@Override
	public String eval() {
		// TODO Auto-generated method stub
		return "" + number;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visitIntLiteral(this);
	}

}
