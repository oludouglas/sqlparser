package org.redlamp.lex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Scanner {

	private InputStream inputStream;
	private char peek;
	private int pos;

	public Scanner(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	public Scanner(String inputString) {
		super();
		this.inputStream = new ByteArrayInputStream(inputString.getBytes());
	}

	public char next() {
		try {
			peek = (char) inputStream.read();
			pos++;
		} catch (IOException e) {
			peek = ' ';
		}
		return peek;
	}

	public char peek() {
		// TODO Auto-generated method stub
		return peek;
	}

	public int pos() {
		return pos;
	}

}
