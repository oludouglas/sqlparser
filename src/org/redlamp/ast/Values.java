package org.redlamp.ast;

public class Values implements ASTNode {

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitValues(this);
	}

}
