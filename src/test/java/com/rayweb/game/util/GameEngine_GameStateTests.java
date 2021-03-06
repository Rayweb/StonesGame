package com.rayweb.game.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.rayweb.game.domain.GameState;
import com.rayweb.game.domain.Player;
import com.rayweb.game.exception.GameStateException;
import com.rayweb.game.exception.InvalidPlayerIdException;
import com.rayweb.game.exception.PlayerAlreadyActiveException;
import com.rayweb.game.util.GameEngine;

public class GameEngine_GameStateTests {

	private GameEngine gameEngine;
	
	@Before
	public void init() {
		gameEngine = new GameEngine();
	}
	
	@Test
	public void resetGame_GameStateReseted() {
		gameEngine.resetGame();
		GameState state = gameEngine.getGame().getState();
		assertThat(state).isEqualTo(GameState.RESTARTED);
	}
	
	@Test
	public void gameStateAfterPlayer1Registration_ShoulBeReady() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameEngine.registerPlayer(Player.PLAYER_1.toString());
		GameState state = gameEngine.getGame().getState();
		assertThat(state).isEqualTo(GameState.READY);
	}
	
	@Test
	public void gameStateAfterPlayer2Registration_ShoulBeReady() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameEngine.registerPlayer(Player.PLAYER_2.toString());
		GameState state = gameEngine.getGame().getState();
		assertThat(state).isEqualTo(GameState.READY);
	}
	
	@Test
	public void gameStateAfter2PlayersRegistered_ShoulBeStarted() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameEngine.registerPlayer(Player.PLAYER_1.toString());
		gameEngine.registerPlayer(Player.PLAYER_2.toString());
		GameState state = gameEngine.getGame().getState();
		assertThat(state).isEqualTo(GameState.STARTED);
	}
	
	@Test
	public void gameStateWithNoActivePlayer_ShoulBeStarted() {
		GameState state = gameEngine.getGame().getState();
		assertThat(state).isEqualTo(GameState.READY);
	}
	
	@Test
	public void gameState_ShouldNotAllowRegistration() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameEngine.registerPlayer(Player.PLAYER_1.toString());
		gameEngine.registerPlayer(Player.PLAYER_2.toString());
		boolean allowsResitration = gameEngine.gameStateAllowsRegistration();
		assertThat(allowsResitration).isFalse();
	}
	
	@Test
	public void gameStaterRestarted_ShouldAllowRegistration() {
		gameEngine.resetGame();
		boolean allowsResitration = gameEngine.gameStateAllowsRegistration();
		assertThat(allowsResitration).isTrue();
	}
}
