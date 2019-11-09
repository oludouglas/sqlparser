package org.redlamp.lex;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Scanner {

	private InputStream inputStream;
	private char peek;
	private int pos = -1;

	public Scanner(InputStream inputStream) {
		super();
		this.inputStream = new BufferedInputStream(inputStream);
	}

	public Scanner(String inputString) {
		super();
		this.inputStream = new ByteArrayInputStream(inputString.getBytes());
	}

	public char next() {
		return next(true);
	}

	char next(boolean count) {
		try {
			if (inputStream.available() > 0)
				peek = (char) inputStream.read();
			if (count)
				pos++;
		} catch (IOException e) {
			peek = ' ';
		}
		return peek;
	}

	public char peek() {
		inputStream.mark(pos);
		char next = next(false);
		try {
			inputStream.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return next;
	}

	public int pos() {
		return pos;
	}

}
