
package org.redlamp.ast;

public abstract class Expr implements AstNode {

	public String name;
	public Identifier lhs, rhs;

	@Override
	public abstract <T> T accept(AstVisitor<T> v);

}
