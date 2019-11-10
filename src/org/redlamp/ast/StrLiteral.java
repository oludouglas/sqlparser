package org.redlamp.ast;

import org.redlamp.ast.BaseType;

public class StrLiteral extends Expr {

	String name;

	public StrLiteral(String name) {
		super();
		this.name = name;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBaseType(BaseType.CHAR);
	}

}
