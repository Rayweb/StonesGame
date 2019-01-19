package com.rayweb.game.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rayweb.game.domain.GameState;
import com.rayweb.game.domain.Pit;
import com.rayweb.game.domain.PitType;
import com.rayweb.game.domain.Player;
import com.rayweb.game.domain.Turn;
import com.rayweb.game.exception.GameStateException;
import com.rayweb.game.exception.InvalidPlayerIdException;
import com.rayweb.game.exception.PlayerAlreadyActiveException;
import com.rayweb.game.util.GameEngine;

public class GameEngine_EndOfGameTests {

	public GameEngine gameEngine;
		
	@Before
	public void init() {
		gameEngine = new GameEngine();
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		for(Pit pit : pits) {
			if(pit.getType().equals(PitType.REGULAR)) {
				pit.setStones(0);
			}
		}
	}

	@Test
	public void endOfGameShouldBeFalse() {
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(7).setStones(1);
		boolean gameEnded = gameEngine.endOfGame();
		assertThat(gameEnded).as("End of game").isFalse();
	}
	
	@Test
	public void endOfGameisFalseWinnerShouldbeNull() {
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		String winner = gameEngine.getGame().getWinner();
		assertThat(winner).as("Winner should not be set yet").isNull();
	}

	@Test
	public void endOfGameShouldBeTrue() {
		boolean gameEnded = gameEngine.endOfGame();
		assertThat(gameEnded).as("End Of game").isTrue();
	}
	
	@Test
	public void ifEndOfGameGameStateShouldBeFinished() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		gameEngine.playNextTurn(turn);
		GameState gameState = gameEngine.getGame().getState();
		assertThat(gameState).as("Game State should be Finished").isEqualTo(GameState.FINISHED);
	}
	
	@Test
	public void endOfGame_Player1HasNoStones() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(12).setStones(1);
		gameEngine.playNextTurn(turn);
		GameState gameState = gameEngine.getGame().getState();
		assertThat(gameState).as("Game State should be Finished").isEqualTo(GameState.FINISHED);
	}
	
	@Test
	public void endOfGame_Player2HasNoStones() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(12, Player.PLAYER_2, 0, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(12).setStones(1);
		gameEngine.playNextTurn(turn);
		GameState gameState = gameEngine.getGame().getState();
		assertThat(gameState).as("Game State should be Finished").isEqualTo(GameState.FINISHED);
	}
	
	
	@Test
	public void ifEndOfGameGameWinnerShouldBePlayer1() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(6).setStones(40);
		pits.get(13).setStones(19);
		gameEngine.playNextTurn(turn);
		String winner = gameEngine.getGame().getWinner();
		assertThat(winner).as("Winner should be Player 1").isEqualTo(Player.PLAYER_1.toString());
	}
	
	@Test
	public void ifEndOfGameGameWinnerShouldBePlayer2() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(6).setStones(19);
		pits.get(13).setStones(40);
		gameEngine.playNextTurn(turn);
		String winner = gameEngine.getGame().getWinner();
		assertThat(winner).as("Winner should be Player 2").isEqualTo(Player.PLAYER_2.toString());
	}
	
	@Test
	public void ifEndOfGameGameWinnerShouldBeDraw() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		pits.get(6).setStones(35);
		pits.get(13).setStones(36);
		gameEngine.playNextTurn(turn);
		String winner = gameEngine.getGame().getWinner();
		assertThat(winner).as("Game should end in Draw").isEqualTo("DRAW");
	}
	
	@Test
	public void gameEnded_ShouldAllowRegistration() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(1);
		gameEngine.playNextTurn(turn);
		boolean allowsResitration = gameEngine.gameStateAllowsRegistration();
		assertThat(allowsResitration).isFalse();
	}
}
