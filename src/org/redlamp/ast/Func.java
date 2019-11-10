package org.redlamp.ast;

import java.util.List;

public class Func implements ASTNode {

	public Expr expr;
	public List<Identifier> args;

	public Func(Expr name, List<Identifier> args) {
		super();
		this.expr = name;
		this.args = args;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitFunc(this);
	}

}
