package org.redlamp.ast;

public interface ASTVisitor<T> {

	public T visitStatement(Stmt bt);

	public T visitBaseType(BaseType bt);

	public T visitSelect(Select bt);

	public T visitValues(Values bt);

	public T visitDelete(Delete bt);

	public T visitInsert(Insert bt);

}
