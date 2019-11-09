package org.redlamp.ast;

public enum BaseType implements Type {
	INT, CHAR, VOID, GT, LT;

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBaseType(this);
	}
}
