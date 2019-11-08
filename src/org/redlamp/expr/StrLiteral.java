package org.redlamp.expr;

public class StrLiteral extends Expr {

	String name;

	public StrLiteral(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toStr() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String eval() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitStrLiteral(this);
	}

}
