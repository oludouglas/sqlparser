package org.redlamp.ast;

public class Select implements ASTNode {

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitSelect(this);
	}

}
