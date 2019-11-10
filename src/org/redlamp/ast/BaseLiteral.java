package org.redlamp.ast;

import org.redlamp.ast.BaseType;

public class BaseLiteral extends Expr {

	String name;

	public BaseLiteral(String name) {
		super();
		this.name = name;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBaseType(BaseType.VOID);
	}

}
