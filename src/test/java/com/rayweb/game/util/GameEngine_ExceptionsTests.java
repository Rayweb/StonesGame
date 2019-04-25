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

public class GameEngine_ExceptionsTests {

	private GameEngine gameEngine;

	@Before
	public void init() {
		gameEngine = new GameEngine();
	}

	@Test
	public void registerPlayerWithInvalidID_throwsInvalidPlayerIdException() {
		try {
			gameEngine.registerPlayer("Player3");
		} catch (Exception ex) {
			assertThat(ex).isInstanceOf(InvalidPlayerIdException.class);
			assertThat(ex.getMessage()).isEqualTo("Invalid Player Id");
		}
	}
	
	@Test
	public void registerPlayer1WhenAlreadyActive_throwsInvalidPlayerIdException() {
		try {
			gameEngine.getGame().setPlayer1Active(true);
			gameEngine.registerPlayer(Player.PLAYER_1.toString());
		} catch (Exception ex) {
			assertThat(ex).isInstanceOf(PlayerAlreadyActiveException.class);
			assertThat(ex.getMessage()).isEqualTo("Player 1 is already Active");
		}
	}
	
	@Test
	public void registerPlayer2WhenAlreadyActive_throwsInvalidPlayerIdException() {
		try {
			gameEngine.getGame().setPlayer2Active(true);
			gameEngine.registerPlayer(Player.PLAYER_2.toString());
		} catch (Exception ex) {
			assertThat(ex).isInstanceOf(PlayerAlreadyActiveException.class);
			assertThat(ex.getMessage()).isEqualTo("Player 2 is already Active");
		}
	}
	
	@Test
	public void registerPlayerOnStateStarted_throwsGameStateException() {
		try {
			gameEngine.getGame().setState(GameState.STARTED);
			gameEngine.registerPlayer(Player.PLAYER_2.toString());
		} catch (Exception ex) {
			assertThat(ex).isInstanceOf(GameStateException.class);
			assertThat(ex.getMessage()).isEqualTo("The current Game state: STARTED does not allow registration");
		}
	}
}
