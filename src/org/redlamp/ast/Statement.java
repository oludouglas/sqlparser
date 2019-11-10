package org.redlamp.ast;

public class Statement implements ASTNode {

	public Use use;
	public Select select;
	public Insert insert;
	public Delete delete;

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitStatement(this);
	}
}
