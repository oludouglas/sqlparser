package org.redlamp.syn;

import org.redlamp.expr.BinOp;
import org.redlamp.expr.IntLiteral;
import org.redlamp.expr.Op;
import org.redlamp.expr.StrLiteral;
import org.redlamp.expr.Visitor;

public class TypeChecker implements Visitor<Type> {

	Type visitVarDecl(VarDecl vd) {
		if (vd.type == Type.VOID) {
			error();
		}
		return null;
	}

	Type visitVarExp(Var v) {
		v.type = v.vd.type;
		return v.vd.type;
	}

	Type visitFuncCal(FuncCall fc) {
		if (fc.type == Type.VOID) {
			error();
		}
		return null;
	}

	Type visitBinOp(BinOp binOp) {
		Type lhsT = binOp.lhs.accept(this);
		Type rhsT = binOp.rhs.accept(this);
		if (binOp.op == Op.ADD) {
			if (lhsT == Type.INT && rhsT == Type.INT) {
				binOp.type = Type.INT; // set the type
				return Type.INT; // return it
			} else
				error();
		}
		return null;
	}

	private void error() {
		// TODO Auto-generated method stub

	}

	@Override
	public Type visitIntLiteral(IntLiteral i) {
		// TODO Auto-generated method stub
		return i.accept(this);
	}

	@Override
	public Type visitBiOp(BinOp bo) {
		// TODO Auto-generated method stub
		return bo.accept(this);
	}

	@Override
	public Type visitStrLiteral(StrLiteral strLiteral) {
		// TODO Auto-generated method stub
		return null;
	}

}
