package com.bol.game.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bol.game.domain.GameState;
import com.bol.game.domain.Pit;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;
import com.bol.game.util.exception.GameStateException;
import com.bol.game.util.exception.InvalidPlayerIdException;
import com.bol.game.util.exception.PlayerAlreadyActiveException;

public class GameEngineTests {

	public GameEngine gameEngine;
	
	public Pit regularPitPlayer1;
	public Pit largePitPlayer1;
	public Pit regularPitPlayer2;
	public Pit largePitPlayer2;

	@Before
	public void init() {
		gameEngine = new GameEngine();
		regularPitPlayer1 = new Pit(0, Player.PLAYER_1, 12, PitType.REGULAR, 6);
		largePitPlayer1 = new Pit(6, Player.PLAYER_1, 13, PitType.LARGE, 0);
		regularPitPlayer2 = new Pit(7, Player.PLAYER_2, 5, PitType.REGULAR, 6);
		largePitPlayer2 = new Pit(13, Player.PLAYER_2, 6, PitType.LARGE, 0);
	}

	@Test
	public void whenTakeStones_PitHasCeroStones() {
		gameEngine.takeStones(0);
		int expected =  gameEngine.getGame().getBoard().getPits().get(regularPitPlayer1.getId()).getStones();
		assertThat(expected).as("There are 0 stones in pit").isEqualTo(0);

	}
	
	@Test
	public void whenDropStone_PitHasOneMoreStone() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		int oldValue = gameEngine.getGame().getBoard().getPits().get(regularPitPlayer1.getId()).getStones();
		gameEngine.dropStone(turn,5);
		int newValue = gameEngine.getGame().getBoard().getPits().get(regularPitPlayer1.getId()).getStones();
		assertThat(oldValue + 1).as("The stone cound increase by one").isEqualTo(newValue);
		assertThat(oldValue).as("The stone cound increase by one").isNotEqualTo(newValue);
	}
	
	@Test
	public void whenPlayNextTurn_moveIsvalid() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		assertThat(gameEngine.isValidMove(turn)).isTrue();
	}
	
	
	@Test
	public void isplayersPitTest() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		assertThat(gameEngine.isPLayersPit(turn)).isTrue();
	}
	
	@Test
	public void isNotPlayersPitTest() {
		Turn turn = new Turn(Player.PLAYER_2, regularPitPlayer1);
		assertThat(gameEngine.isPLayersPit(turn)).isFalse();
	}
	
	@Test
	public void pitHasStones_shouldBeTrue() {
		assertThat(gameEngine.pitHasStones(regularPitPlayer1.getId())).isTrue();
	}
	
	@Test
	public void pitHasStones_shouldBeFalse() {
		gameEngine.takeStones(regularPitPlayer1.getId());
		assertThat(gameEngine.pitHasStones(regularPitPlayer1.getId())).isFalse();
	}
	
	@Test
	public void canCaptureStones_shouldBeTrue() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(11).setStones(1);
		boolean canCapture = gameEngine.canCapture(turn);
		assertThat(canCapture).isTrue();
 	}
	
	@Test
	public void canCaptureStones_shouldFalse() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(11).setStones(1);
		boolean canCapture = gameEngine.canCapture(turn);
		assertThat(canCapture).isFalse();
 	}
	
	@Test
	public void player2CaptureStones_shouldHaveStonesInBigPit() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(11).setStones(1);
		gameEngine.captureStones(turn);
		int stonesInPlayer2BigPit = pits.get(13).getStones();
		assertThat(stonesInPlayer2BigPit).isEqualTo(7);
 	}
	
	@Test
	public void isValidMove_ShouldBeFalse() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 0));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(11).setStones(0);
		boolean expected = gameEngine.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
	
	@Test
	public void isValidMove_ShouldBeFalseOnLargePitAttempt() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(13, Player.PLAYER_2, 1, PitType.LARGE, 0));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(13).setStones(5);
		boolean expected = gameEngine.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
	
	@Test
	public void isValidMove_ShouldBeFalseOnOponentsPitAttempt() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(0, Player.PLAYER_1, 1, PitType.REGULAR, 0));
		boolean expected = gameEngine.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
}

