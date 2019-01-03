package com.bol.game.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bol.game.domain.Pit;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;

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
	public void whenPlayNextTurn_lastStoneInOwnBigPit_getNewTurn() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		gameEngine.playNextTurn(turn);
		assertThat(gameEngine.getGame().getNextTurn()).as("Same Player Turn as falls in own large pit").isEqualTo(Player.PLAYER_1);

	}
	
	@Test
	public void whenPlayNextTurn_isNextPlayerTurn() {
		Turn turn = new Turn(Player.PLAYER_2, gameEngine.getGame().getBoard().getPits().get(8));
		gameEngine.playNextTurn(turn);
		assertThat(gameEngine.getGame().getNextTurn()).as("Is next Player turn").isEqualTo(Player.PLAYER_1);

	}
	
	@Test
	public void whenPlayNextTurn_StonesAddedToNextPits() {
		Turn turn = new Turn(Player.PLAYER_1, regularPitPlayer1);
		gameEngine.playNextTurn(turn);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		int stonesCountInpit0 = pits.get(0).getStones();
		int stonesCountInpit1 = pits.get(1).getStones();
		int stonesCountInpit2 = pits.get(2).getStones();
		int stonesCountInpit3 = pits.get(3).getStones();
		int stonesCountInpit4 = pits.get(4).getStones();
		int stonesCountInpit5 = pits.get(5).getStones();
		int stonesCountInpit6 = pits.get(6).getStones();
		int stonesCountInpit7 = pits.get(7).getStones();
		assertThat(stonesCountInpit0).as("Pit 0 should have 0 Stones").isEqualTo(0);
		assertThat(stonesCountInpit1).as("Pit 1 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit2).as("Pit 2 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit3).as("Pit 3 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit4).as("Pit 4 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit5).as("Pit 5 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit6).as("Pit 6 should have 1 Stone").isEqualTo(1);
		assertThat(stonesCountInpit7).as("Pit 7 should have 6 Stones Unafected by this move)").isEqualTo(6);
	}
	
	@Test
	public void whenPlayNextTurnFormPit1_StonesAddedToNextPits() {
		Turn turn = new Turn(Player.PLAYER_1, gameEngine.getGame().getBoard().getPits().get(1));
		gameEngine.playNextTurn(turn);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		int stonesCountInpit1 = pits.get(1).getStones();
		int stonesCountInpit2 = pits.get(2).getStones();
		int stonesCountInpit3 = pits.get(3).getStones();
		int stonesCountInpit4 = pits.get(4).getStones();
		int stonesCountInpit5 = pits.get(5).getStones();
		int stonesCountInpit6 = pits.get(6).getStones();
		int stonesCountInpit7 = pits.get(7).getStones();
		assertThat(stonesCountInpit1).as("Pit 1 should have 0 Stones").isEqualTo(0);
		assertThat(stonesCountInpit2).as("Pit 2 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit3).as("Pit 3 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit4).as("Pit 4 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit5).as("Pit 5 should have 7 Stones").isEqualTo(7);
		assertThat(stonesCountInpit6).as("Pit 6 should have 1 Stones").isEqualTo(1);
		assertThat(stonesCountInpit7).as("Pit 7 should have 7 Stones").isEqualTo(7);
	}
	
	@Test
	public void whenPlayNextTurnFormPit0_with13Stones_shouldHave1Stone() {
		Turn turn = new Turn(Player.PLAYER_1,regularPitPlayer1);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(0).setStones(13);
		gameEngine.playNextTurn(turn);
		int stonesCountInpit0 = pits.get(0).getStones();
		assertThat(stonesCountInpit0).as("Pit 0 should have 0 Stones").isEqualTo(0);
	}
	
	@Test
	public void whenPlayNextTurnFormPit0_with12Stones_shouldHaveNoStones() {
		Turn turn = new Turn(Player.PLAYER_1,regularPitPlayer1);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(0).setStones(12);
		gameEngine.playNextTurn(turn);
		int stonesCountInpit0 = pits.get(0).getStones();
		assertThat(stonesCountInpit0).as("Pit 0 should have 0 Stones").isEqualTo(0);
	}
	
	
	@Test
	public void whenPlayNextTurnFormPit1_Pit7ShouldHave7Stones() {
		Turn turn = new Turn(Player.PLAYER_1,gameEngine.getGame().getBoard().getPits().get(1));
		gameEngine.playNextTurn(turn);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		int stonesCountInpit7 = pits.get(7).getStones();
		assertThat(stonesCountInpit7).as("Pit 7 should have 7 Stones").isEqualTo(7);
	}
	
	
	@Test
	public void whenPlayNextTurnFormPit5_and_8Stones_OponentPitHas0Stones() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(8);
		gameEngine.playNextTurn(turn);
		int stonesCountInpit13 = pits.get(5).getStones();
		assertThat(stonesCountInpit13).as("Pit 13 should have 0 Stones").isEqualTo(0);
	}
	
	@Test
	public void whenPlayNextTurnFormPit7_and_8Stones_Player1Pit0Has7Stones() {
		Turn turn = new Turn(Player.PLAYER_2, regularPitPlayer2);
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(7).setStones(8);
		gameEngine.playNextTurn(turn);
		int stonesCountInpit0 = pits.get(0).getStones();
		assertThat(stonesCountInpit0).as("Pit 0 should have 7 Stones").isEqualTo(7);
	}
	
	
	@Test
	public void whenPlayNextTurnFormPit5_and_8Stones_Player1Pit1Has7Stones() {
		Turn turn = new Turn(Player.PLAYER_1, new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, 6));
		List<Pit> pits = gameEngine.getGame().getBoard().getPits();
		pits.get(5).setStones(8);
		gameEngine.playNextTurn(turn);
		int stonesCountInpit0 = pits.get(0).getStones();
		assertThat(stonesCountInpit0).as("Pit 0 should have 7 Stones").isEqualTo(7);
	}
	
	@Test
	public void whenTakeStones_PitHasCeroStones() {
		gameEngine.takeStones(0);
		int expected =  gameEngine.getGame().getBoard().getPits().get(regularPitPlayer1.getId()).getStones();
		assertThat(expected).as("There are 0 stones in pit").isEqualTo(0);

	}
	
	@Test
	public void whenDropStone_PitHasOneMoreStone() {
		int oldValue = gameEngine.getGame().getBoard().getPits().get(regularPitPlayer1.getId()).getStones();
		gameEngine.dropStone(0);
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
	public void pitHasStones() {
		assertThat(gameEngine.pitHasStones(regularPitPlayer1.getId())).isTrue();
	}
	
	@Test
	public void pitHasNoStones() {
		gameEngine.takeStones(regularPitPlayer1.getId());
		assertThat(gameEngine.pitHasStones(regularPitPlayer1.getId())).isFalse();
	}
	
	
}
