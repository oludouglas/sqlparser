package org.redlamp.expr;

public abstract class Expr {

	public abstract String toStr();

	public abstract String eval();

	public abstract <T> T accept(Visitor<T> v);

}
