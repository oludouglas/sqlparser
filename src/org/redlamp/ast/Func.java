package org.redlamp.ast;

import java.util.List;

public class Func {

	public String name;
	public List<Identifier> args;

	public Func(String name, List<Identifier> args) {
		super();
		this.name = name;
		this.args = args;
	}

}
