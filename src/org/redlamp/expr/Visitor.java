package org.redlamp.expr;

public interface Visitor<T> {

	T visitIntLiteral(IntLiteral i);

	T visitBiOp(BinOp bo);

}
