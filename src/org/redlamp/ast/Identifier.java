package org.redlamp.ast;

public class Identifier implements ASTNode {

	public final String identifierName;

	public Identifier(String varName) {
		this.identifierName = varName;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitIdentifier(this);
	}
}
