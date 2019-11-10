
package org.redlamp.ast;

public interface Type extends ASTNode {

	public <T> T accept(ASTVisitor<T> v);

}
