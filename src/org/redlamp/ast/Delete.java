package org.redlamp.ast;

public class Delete implements ASTNode {

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitDelete(this);
	}

}
