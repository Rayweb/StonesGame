package com.bol.game.exception;

public class PlayerAlreadyActiveException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PlayerAlreadyActiveException(String msg) {
		 super(msg);
	}
	
}
