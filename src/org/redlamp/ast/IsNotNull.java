package org.redlamp.ast;

public class IsNotNull extends Expr {

	public String Identifier;

	public IsNotNull(String identifier) {
		super();
		Identifier = identifier;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitIsNotNull(this);
	}

}
