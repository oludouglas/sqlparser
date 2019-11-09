package org.redlamp.expr;

import org.redlamp.ast.ASTVisitor;
import org.redlamp.ast.BaseType;
import org.redlamp.ast.Expr;

public class IntLiteral extends Expr {

	int number;

	public IntLiteral(int i) {
		super();
		this.number = i;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitBaseType(BaseType.INT);
	}

}
