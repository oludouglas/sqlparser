package org.redlamp.ast;

public interface AstVisitor<T> {

	public T visitTable(Table bt);

	public T visitOrderByExpr(OrderByExpr bt);

	public T visitStatement(Statement bt);

	public T visitIsNotNull(IsNotNull bt);

	public T visitSelect(Select bt);

	public T visitDelete(Delete bt);

	public T visitInsert(Insert bt);

	public T visitIdentifier(Identifier bt);

	public T visitBaseType(BaseType baseType);

	public T visitUse(Use baseType);

	public T visitFunc(Func func);

	public T visitExpr(Expr func);
	
	public T visitWhereClause(WhereClause func);

}
