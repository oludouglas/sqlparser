package org.redlamp.expr;

public class IntLiteral extends Expr {

	int i;

	public IntLiteral(int i) {
		super();
		this.i = i;
	}

	@Override
	public String toStr() {
		// TODO Auto-generated method stub
		return "" + i;
	}

	@Override
	public int eval() {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitIntLiteral(this);
	}

}
