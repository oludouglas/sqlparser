package org.redlamp.ast;

public class IsNotNull implements ASTNode {

	public String Identifier;

	public IsNotNull(String identifier) {
		super();
		Identifier = identifier;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitIsNotNull(this);
	}

}
