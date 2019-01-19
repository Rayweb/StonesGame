package com.rayweb.game.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rayweb.game.domain.Board;
import com.rayweb.game.domain.Pit;
import com.rayweb.game.domain.PitType;
import com.rayweb.game.domain.Player;
import com.rayweb.game.domain.Turn;
import com.rayweb.game.util.GameEngine;

public class BoardTests {
	
	public GameEngine gameEngine;
	public Board board;
	
	public Pit regularPitPlayer1;
	public Pit largePitPlayer1;
	public Pit regularPitPlayer2;
	public Pit largePitPlayer2;
	
	@Before
	public void init() {
		gameEngine = new GameEngine();
		board = gameEngine.getGame().getBoard();
		regularPitPlayer1 = new Pit(0, Player.PLAYER_1, 12, PitType.REGULAR, 6);
		largePitPlayer1 = new Pit(6, Player.PLAYER_1, 13, PitType.LARGE, 0);
		regularPitPlayer2 = new Pit(7, Player.PLAYER_2, 5, PitType.REGULAR, 6);
		largePitPlayer2 = new Pit(13, Player.PLAYER_2, 6, PitType.LARGE, 0);
	}
	
	@Test
	public void whenTakeStones_PitHasCeroStones() {
		gameEngine.getGame().getBoard().takeStones(0);
		int expected =  board.getPits().get(regularPitPlayer1.getId()).getStones();
		assertThat(expected).as("There are 0 stones in pit").isEqualTo(0);

	}
	
	@Test
	public void whenDropStone_PitHasOneMoreStone() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		int oldValue = board.getPits().get(regularPitPlayer1.getId()).getStones();
		board.dropStone(turn,5);
		int newValue = board.getPits().get(regularPitPlayer1.getId()).getStones();
		assertThat(oldValue + 1).as("The stone cound increase by one").isEqualTo(newValue);
		assertThat(oldValue).as("The stone cound increase by one").isNotEqualTo(newValue);
	}
	
	@Test
	public void whenPlayNextTurn_moveIsvalid() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		assertThat(board.isValidMove(turn)).isTrue();
	}
	
	@Test
	public void isplayersPitTest() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		assertThat(board.isPLayersPit(turn)).isTrue();
	}
	
	@Test
	public void isNotPlayersPitTest() {
		Turn turn = new Turn(Player.PLAYER_2, regularPitPlayer1);
		assertThat(board.isPLayersPit(turn)).isFalse();
	}
	
	@Test
	public void pitHasStones_shouldBeTrue() {
		assertThat(board.pitHasStones(regularPitPlayer1.getId())).isTrue();
	}
	
	@Test
	public void pitHasStones_shouldBeFalse() {
		board.takeStones(regularPitPlayer1.getId());
		assertThat(board.pitHasStones(regularPitPlayer1.getId())).isFalse();
	}
	
	@Test
	public void canCaptureStones_shouldBeTrue() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = board.getPits();
		pits.get(11).setStones(1);
		boolean canCapture = board.canCapture(turn);
		assertThat(canCapture).isTrue();
 	}
	
	@Test
	public void canCaptureStones_shouldFalse() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = board.getPits();
		pits.get(11).setStones(1);
		boolean canCapture = board.canCapture(turn);
		assertThat(canCapture).isFalse();
 	}
	
	@Test
	public void player2CaptureStones_shouldHaveStonesInBigPit() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 1));
		List<Pit> pits = board.getPits();
		pits.get(11).setStones(1);
		board.captureStones(turn);
		int stonesInPlayer2BigPit = pits.get(13).getStones();
		assertThat(stonesInPlayer2BigPit).isEqualTo(7);
 	}
	
	@Test
	public void isValidMove_ShouldBeFalse() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, 0));
		List<Pit> pits = board.getPits();
		pits.get(11).setStones(0);
		boolean expected = board.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
	
	@Test
	public void isValidMove_ShouldBeFalseOnLargePitAttempt() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(13, Player.PLAYER_2, 1, PitType.LARGE, 0));
		List<Pit> pits = board.getPits();
		pits.get(13).setStones(5);
		boolean expected = board.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
	
	@Test
	public void isValidMove_ShouldBeFalseOnOponentsPitAttempt() {
		Turn turn = new Turn(Player.PLAYER_2, new Pit(0, Player.PLAYER_1, 1, PitType.REGULAR, 0));
		boolean expected = board.isValidMove(turn);
		assertThat(expected).as("Pit has no stones").isFalse();
	}
}
