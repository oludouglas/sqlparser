package org.redlamp.ast;

import org.redlamp.ast.BaseType;

public class IntLiteral extends Expr {

	int number;

	public IntLiteral(int i) {
		this.number = i;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitBaseType(BaseType.INT);
	}

}
