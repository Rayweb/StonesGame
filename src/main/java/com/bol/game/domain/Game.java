package com.bol.game.domain;

import lombok.Data;

@Data
public class Game {
	public Board board;
	public GameState state;
	public Player nextTurn;
	public boolean player1Active;
	public boolean player2Active;
	private String winner;
}
