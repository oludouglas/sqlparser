package org.redlamp.ast;

public enum BaseType implements Type {
	INT, CHAR, VOID, OPERATOR, VARCHAR;

	public <T> T accept(AstVisitor<T> v) {
		return v.visitBaseType(this);
	}
}
