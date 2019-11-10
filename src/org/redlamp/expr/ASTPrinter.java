package org.redlamp.expr;

import java.io.PrintWriter;
import java.util.List;

import org.redlamp.ast.ASTVisitor;
import org.redlamp.ast.BaseType;
import org.redlamp.ast.Delete;
import org.redlamp.ast.Expr;
import org.redlamp.ast.Func;
import org.redlamp.ast.Identifier;
import org.redlamp.ast.Insert;
import org.redlamp.ast.IsNotNull;
import org.redlamp.ast.OrderByExpr;
import org.redlamp.ast.Select;
import org.redlamp.ast.Statement;
import org.redlamp.ast.Table;
import org.redlamp.ast.Use;
import org.redlamp.ast.WhereClause;

public class ASTPrinter implements ASTVisitor<Void> {

	private PrintWriter writer;

	public ASTPrinter(PrintWriter writer) {
		this.writer = writer;
	}

	@Override
	public Void visitBaseType(BaseType bt) {
		writer.print(" UnnamedExpr(");
		writer.print(bt.name());
		writer.print(")");
		writer.flush();
		return null;
	}

	@Override
	public Void visitTable(Table table) {
		writer.print("Table: (");
		writer.print(table.relation);
		writer.print(")");
		writer.flush();
		return null;
	}

	@Override
	public Void visitOrderByExpr(OrderByExpr ob) {

		writer.print("order_by: [");
		writer.print("OrderByExpr: [");

		ob.expr.accept(this);
		writer.print(",");
		writer.print("asc: ");
		writer.print(ob.asc);

		writer.print("]");
		writer.print("]");

		writer.flush();
		return null;
	}

	@Override
	public Void visitStatement(Statement stmt) {
		writer.print("Statement: [");
		stmt.use.accept(this);
		stmt.select.accept(this);
		stmt.insert.accept(this);
		stmt.delete.accept(this);
		writer.print("]");
		writer.flush();
		return null;
	}

	@Override
	public Void visitIsNotNull(IsNotNull nn) {
		writer.print("IsNotNull(");
		writer.print(nn.Identifier);
		writer.print(")");
		writer.flush();
		return null;
	}

	@Override
	public Void visitSelect(Select select) {

		writer.print("Select[");
		String delimiter = "";

		for (Identifier std : select.identifiers) {
			writer.print(delimiter);
			delimiter = ", ";
			std.accept(this);
		}
		for (Table vd : select.relations) {
			writer.print(delimiter);
			delimiter = ", ";
			vd.accept(this);
		}
		writer.print(delimiter);
		select.conditions.accept(this);

//		select.orderByExprs.accept(this);

		writer.print("]");
		writer.flush();

		return null;
	}

	@Override
	public Void visitDelete(Delete dl) {

		writer.print("Delete: [");
		String delimiter = "";

		for (String std : dl.columns) {
			writer.print(delimiter);
			delimiter = ",";
			writer.print(std);
		}
		dl.table.accept(this);

		for (Expr fd : dl.conditions) {
			writer.print(delimiter);
			delimiter = ",";
			fd.accept(this);
		}

		writer.print("]");
		writer.flush();

		return null;
	}

	@Override
	public Void visitInsert(Insert in) {

		writer.print("Insert: [");
		writer.print("relation: {");
		in.relation.accept(this);
		writer.print("}");

		writer.print(", ");

		writer.print("{columns: [");
		in.columns.accept(this);
		writer.print("]}");

		writer.print(", {values: [");
		in.values.accept(this);
		writer.print("]}");

		writer.print("]");
		writer.flush();

		return null;
	}

	@Override
	public Void visitIdentifier(Identifier id) {

		writer.print("Identifier(");
		writer.print(id.identifierName);

		writer.print(")");
		writer.flush();

		return null;
	}

	@Override
	public Void visitUse(Use use) {
		writer.print("[");

		writer.print(use.command);
		writer.print(":{");
		writer.print("database: " + use.database);
		writer.print("}");
		writer.print("]");
		writer.flush();
		return null;
	}

	public String name;
	public List<Identifier> args;

	@Override
	public Void visitFunc(Func func) {

		String delimiter = "";
		for (Identifier fd : func.args) {
			writer.print(delimiter);
			delimiter = ", ";
			fd.accept(this);
		}

		writer.print(")");
		writer.flush();

		return null;
	}

	@Override
	public Void visitExpr(Expr expr) {
		writer.print("Expression: [");
		expr.accept(this);
		writer.print("]");
		writer.flush();
		return null;
	}

	@Override
	public Void visitWhereClause(WhereClause func) {
		writer.print("Where: [");

		for (Expr fd : func.expr) {
			writer.print(" ");
			fd.accept(this);
		}

		writer.print("]");
		writer.flush();
		return null;
	}

}
