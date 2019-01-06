package com.bol.game.service;

import org.springframework.stereotype.Service;

import com.bol.game.domain.Game;
import com.bol.game.domain.Turn;
import com.bol.game.util.GameEngine;

@Service
public class GameService {
	
	private GameEngine gameEngine;

	public GameService(GameEngine gameengine) {
		this.gameEngine = gameengine;
	}
	
	public void move(Turn turn){
		this.gameEngine.playNextTurn(turn);
	}

	public Game getNewGame(){
		return this.gameEngine.getGame();
	}
	
	public Game getGame(){
		return this.gameEngine.getGame();
	}
	
	public void playNextTurn(Turn turn){
		this.gameEngine.playNextTurn(turn);
	}
}
