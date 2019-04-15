package com.rayweb.game.service;

import org.springframework.stereotype.Service;

import com.rayweb.game.domain.Game;
import com.rayweb.game.domain.Turn;
import com.rayweb.game.exception.GameStateException;
import com.rayweb.game.exception.InvalidPlayerIdException;
import com.rayweb.game.exception.PlayerAlreadyActiveException;
import com.rayweb.game.util.GameEngine;

@Service
public class GameService {
	
	private GameEngine gameEngine;

	public GameService(final GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	public void resetGame(){
		this.gameEngine.resetGame();
	}
	
	public Game getGame(){
		return this.gameEngine.getGame();
	}
	
	public void playNextTurn(final Turn turn){
		this.gameEngine.playNextTurn(turn);
	}
	
	public void registerPlayer(final String playerId) throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		this.gameEngine.registerPlayer(playerId);
	}
}
