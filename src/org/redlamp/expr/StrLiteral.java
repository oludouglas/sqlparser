package org.redlamp.expr;

import org.redlamp.ast.ASTVisitor;
import org.redlamp.ast.BaseType;
import org.redlamp.ast.Expr;

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
