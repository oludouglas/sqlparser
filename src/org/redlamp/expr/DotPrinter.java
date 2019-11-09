package org.redlamp.expr;

import java.io.PrintWriter;

public class DotPrinter implements Visitor<String> {

	PrintWriter writer;
	int nodeCnt = 0;

	public DotPrinter(PrintWriter writer) {
		super();
		this.writer = writer;
	}

	@Override
	public String visitIntLiteral(IntLiteral i) {
		nodeCnt++;
		writer.println("Node" + nodeCnt + "[label: \"Cst(" + i.number + ")\"]];");
		return "Node" + nodeCnt;
	}

	@Override
	public String visitBiOp(BinOp bo) {
		String binOpNodeId = "Node" + nodeCnt++;
		writer.println(binOpNodeId + "[label:\"BinOp\"];");

		String lhsNodeID = bo.lhs.accept(this);
		String opNodeID = "Node" + nodeCnt++;
		writer.println(opNodeID + "[label=\"+\"];");
		String rhsNodeID = bo.rhs.accept(this);

		writer.println(binOpNodeId + " -> " + lhsNodeID + ";");
		writer.println(binOpNodeId + " -> " + opNodeID + ";");
		writer.println(binOpNodeId + " -> " + rhsNodeID + ";");
		return binOpNodeId;
	}

	@Override
	public String visitStrLiteral(StrLiteral strLiteral) {
		// TODO Auto-generated method stub
		return null;
	}

}
