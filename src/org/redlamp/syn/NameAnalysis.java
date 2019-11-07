package org.redlamp.syn;

public class NameAnalysis implements ASTVisitor<Void> {

	Scope scope;

	public NameAnalysis(Scope scope) {
		super();
		this.scope = scope;
	}

	public Void visitVarDecl(VarDecl vd) {
		Symbol s = scope.lookupCurrent(vd.var.name);
		if (s != null)
			error();
		else
			scope.put(new VarSymbol(vd));
		return null;
	}

	public Void visitBlock(Block b) {
		Scope oldScope = scope;
		scope = new Scope(oldScope);
		// visit the children

		scope = oldScope;
		return null;
	}

	public Void visitVar(Var v) {
		Symbol s = scope.lookup(v.name);
		if (s == null)
			error();
		else if (!s.isVar())
			error();
		else
			v.vd = ((VarSymbol) s).vd;
		return null;
	}

	private void error() {
		// TODO Auto-generated method stub
	}

}
