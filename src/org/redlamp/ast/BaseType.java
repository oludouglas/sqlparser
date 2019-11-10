package org.redlamp.ast;

public enum BaseType implements Type {
	INT(""), CHAR(""), VOID(""), OPERATOR("");

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBaseType(this);
	}

	public final String value;

	BaseType(String data) {
		this.value = data;
	}
}
