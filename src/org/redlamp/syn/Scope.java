package org.redlamp.syn;

import java.util.HashMap;
import java.util.Map;

public class Scope {

	Scope outer;
	Map<String, Symbol> symboleTable = new HashMap<String, Symbol>();

	public Scope(Scope outer) {
		super();
		this.outer = outer;
	}

	Symbol lookup(String name) {
		return symboleTable.get(name);
	}

	Symbol lookupCurrent(String name) {
		return symboleTable.get(name);
	}

	void put(Symbol symbol) {
		symboleTable.put(symbol.name, symbol);
	}

}
