package org.redlamp.ast;

public class Insert implements ASTNode {

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitInsert(this);
	}

}
