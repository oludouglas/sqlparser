package org.redlamp.ast;

import org.redlamp.ast.BaseType;

public class StrLiteral extends Expr {

	String name;

	public StrLiteral(String name) {
		this.name = name;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		return v.visitBaseType(BaseType.VARCHAR);
	}

}
