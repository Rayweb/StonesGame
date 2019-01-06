package com.bol.game.service;

import org.springframework.stereotype.Service;

import com.bol.game.domain.Game;
import com.bol.game.domain.Turn;
import com.bol.game.util.GameEngine;
import com.bol.game.util.exception.GameStateException;
import com.bol.game.util.exception.InvalidPlayerIdException;
import com.bol.game.util.exception.PlayerAlreadyActiveException;

@Service
public class GameService {
	
	private GameEngine gameEngine;

	public GameService(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	public void move(Turn turn){
		this.gameEngine.playNextTurn(turn);
	}

	public void resetGame(){
		this.gameEngine.resetGame();
	}
	
	public Game getGame(){
		return this.gameEngine.getGame();
	}
	
	public void playNextTurn(Turn turn){
		this.gameEngine.playNextTurn(turn);
	}
	
	public void registerPlayer(String playerId) throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameEngine.registerPlayer(playerId);
	}
}
