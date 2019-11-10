
package org.redlamp.ast;

public abstract class Expr implements ASTNode {
	
	public String name;

	@Override
	public abstract <T> T accept(ASTVisitor<T> v);

}
