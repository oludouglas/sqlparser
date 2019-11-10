
package org.redlamp.ast;

public abstract class Expr implements ASTNode {

	public String name;
	public Identifier lhs, rhs;

	@Override
	public abstract <T> T accept(ASTVisitor<T> v);

}
