package com.bol.game.exception;

public class InvalidPlayerIdException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidPlayerIdException(String msg) {
        super(msg);
    }
}
