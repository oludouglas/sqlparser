package org.redlamp.ast;

import java.util.List;

public class Select implements AstNode {

	public final List<Identifier> identifiers;
	public List<Table> relations;
	public WhereClause conditions;

	public OrderByExpr orderByExprs;

	public Select(List<Identifier> identifiers, List<Table> relations, WhereClause conditions,
			OrderByExpr orderByExprs) {
		super();
		this.identifiers = identifiers;
		this.relations = relations;
		this.conditions = conditions;
		this.orderByExprs = orderByExprs;
	}

	@Override
	public <T> T accept(AstVisitor<T> v) {
		return v.visitSelect(this);
	}

}
