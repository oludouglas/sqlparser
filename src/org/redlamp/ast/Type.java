
package org.redlamp.ast;

public interface Type extends AstNode {

	public <T> T accept(AstVisitor<T> v);

}
