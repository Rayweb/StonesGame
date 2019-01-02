package com.bol.game.service;

import com.bol.game.domain.Turn;
import com.bol.game.util.GameEngine;

public class GameService {
	
	
	private GameEngine gameEngine;

	public GameService(GameEngine gameengine) {
		this.gameEngine = gameengine;
	}
	
	public void move(Turn turn){
		this.gameEngine.playNextTurn(turn);
	}

}
