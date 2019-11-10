package org.redlamp.ast;

public interface AstNode {
	public <T> T accept(AstVisitor<T> v);
}
