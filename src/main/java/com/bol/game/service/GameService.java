package com.bol.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bol.game.domain.Game;
import com.bol.game.domain.Turn;
import com.bol.game.exception.GameStateException;
import com.bol.game.exception.InvalidPlayerIdException;
import com.bol.game.exception.PlayerAlreadyActiveException;
import com.bol.game.util.GameEngine;

@Service
public class GameService {
	
	@Autowired
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
