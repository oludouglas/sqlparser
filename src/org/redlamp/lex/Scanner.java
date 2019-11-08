package org.redlamp.lex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class Scanner {

	private PushbackInputStream inputStream;
	private char peek;
	private int pos = -1;

	public Scanner(InputStream inputStream) {
		super();
		this.inputStream = new PushbackInputStream(inputStream);
	}

	public Scanner(String inputString) {
		super();
		this.inputStream = new PushbackInputStream(new ByteArrayInputStream(inputString.getBytes()));
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
		char next = next();
		try {
			inputStream.unread(next);
			pos--;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return next;
	}

	public int pos() {
		return pos;
	}

}
