package org.redlamp.ast;

import org.redlamp.lex.Token;
import org.redlamp.lex.Token.TokenClass;

public class Identifier implements ASTNode {

	public TokenClass identifierType;
	public final String identifierName;

	public Identifier(String varName) {
		this.identifierName = varName;
	}

	public Identifier(Token token) {
		this.identifierName = token.data;
		this.identifierType = token.tokenClass;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitIdentifier(this);
	}
}
