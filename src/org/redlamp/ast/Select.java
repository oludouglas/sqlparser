package org.redlamp.ast;

import java.util.List;

public class Select implements ASTNode {

	public final List<Identifier> identifiers;
	public List<Table> relations;
	public List<Expr> conditions;
	public OrderByExpr orderByExprs;

	public Select(List<Identifier> identifiers, List<Table> relations, List<Expr> conditions,
			OrderByExpr orderByExprs) {
		super();
		this.identifiers = identifiers;
		this.relations = relations;
		this.conditions = conditions;
		this.orderByExprs = orderByExprs;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visitSelect(this);
	}

}
