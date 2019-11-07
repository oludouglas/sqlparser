package org.redlamp.syn;

public abstract class Symbol {

	String name;

	boolean isVar() {

		return false;
	}

	boolean isProc() {

		return false;
	}

}
