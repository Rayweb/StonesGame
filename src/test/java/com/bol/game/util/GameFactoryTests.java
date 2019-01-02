package com.bol.game.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.bol.game.domain.Game;
import com.bol.game.domain.GameState;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;

public class GameFactoryTests {

	@Test
	public void whenNewGameIsCreated_GameShouldNotBeNull() {
		Game game = GameFactory.newGame();
		assertNotNull("The Game should not be null", game);
		assertThat(game).isNotNull();
	}

	@Test
	public void whenNewGameIsCreated_GameStateIsReady() {
		Game game = GameFactory.newGame();
		GameState expected = GameState.READY;
		GameState actual = game.getState();
		assertEquals("The GameState property should be READY", expected, actual);
	}

	@Test
	public void whenNewGameIsCreated_IsPlayer1Turn() {
		Game game = GameFactory.newGame();
		Player expected = Player.PLAYER_1;
		Player actual = game.getNextTurn();
		assertEquals("The NextTurn property should be PLAYER_1", expected, actual);
	}
	
	@Test
	public void whenNewGameIsCreated_BoardHas14Pits() {
		Game game = GameFactory.newGame();
		Integer expected = 14;
		Integer actual = game.getBoard().getPits().size();
		assertEquals("The Game Board should have 14 pits", expected, actual);
	}
	
	@Test
	public void whenNewGameIsCreated_RegularPitsHave6Stones() {
		Game game = GameFactory.newGame();
		Integer expected = 6;
		assertEquals("The Pit 0 should have 6 Stones", expected, game.getBoard().getPits().get(0).getStones());
		assertEquals("The Pit 1 should have 6 Stones", expected, game.getBoard().getPits().get(1).getStones());
		assertEquals("The Pit 2 should have 6 Stones", expected, game.getBoard().getPits().get(2).getStones());
		assertEquals("The Pit 3 should have 6 Stones", expected, game.getBoard().getPits().get(3).getStones());
		assertEquals("The Pit 4 should have 6 Stones", expected, game.getBoard().getPits().get(4).getStones());
		assertEquals("The Pit 5 should have 6 Stones", expected, game.getBoard().getPits().get(5).getStones());
		assertEquals("The Pit 7 should have 6 Stones", expected, game.getBoard().getPits().get(7).getStones());
		assertEquals("The Pit 8 should have 6 Stones", expected, game.getBoard().getPits().get(8).getStones());
		assertEquals("The Pit 9 should have 6 Stones", expected, game.getBoard().getPits().get(9).getStones());
		assertEquals("The Pit 10 should have 6 Stones", expected, game.getBoard().getPits().get(10).getStones());
		assertEquals("The Pit 11 should have 6 Stones", expected, game.getBoard().getPits().get(11).getStones());
		assertEquals("The Pit 12 should have 6 Stones", expected, game.getBoard().getPits().get(12).getStones());
	}
	
	@Test
	public void whenNewGameIsCreated_LargePitsHave0_Stones() {
		Game game = GameFactory.newGame();
		Integer expected = 0;
		assertEquals("The Large Pit 6 should have 0 Stones", expected, game.getBoard().getPits().get(6).getStones());
		assertEquals("The Large Pit 13 should have 0 Stones", expected, game.getBoard().getPits().get(13).getStones());
	}
	
	@Test
	public void whenNewGameIsCreated_Player1_OnwsFirst6Pits() {
		Game game = GameFactory.newGame();
		Player expected = Player.PLAYER_1;
		assertEquals("The owner of Pit 0 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(0).getOwner());
		assertEquals("The owner of Pit 1 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(1).getOwner());
		assertEquals("The owner of Pit 2 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(2).getOwner());
		assertEquals("The owner of Pit 3 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(3).getOwner());
		assertEquals("The owner of Pit 4 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(4).getOwner());
		assertEquals("The owner of Pit 5 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(5).getOwner());
		assertNotEquals("The owner of Pit 0 should not be Player.PLAYER_2", Player.PLAYER_2, game.getBoard().getPits().get(0).getOwner());
	}
	
	@Test
	public void whenNewGameIsCreated_Player2_Onws6Pits() {
		Game game = GameFactory.newGame();
		Player expected = Player.PLAYER_2;
		assertEquals("The owner of Pit 7 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(7).getOwner());
		assertEquals("The owner of Pit 8 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(8).getOwner());
		assertEquals("The owner of Pit 9 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(9).getOwner());
		assertEquals("The owner of Pit 10 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(10).getOwner());
		assertEquals("The owner of Pit 11 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(11).getOwner());
		assertEquals("The owner of Pit 12 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(12).getOwner());
		assertNotEquals("The owner of Pit 7 should not be Player.PLAYER_1", Player.PLAYER_1, game.getBoard().getPits().get(7).getOwner());
	}
	
	@Test
	public void whenNewGameIsCreated_Player1_OnwsLargePit6() {
		Game game = GameFactory.newGame();
		Player expected = Player.PLAYER_1;
		assertEquals("The owner of Pit 6 should be Player.PLAYER_1", expected, game.getBoard().getPits().get(6).getOwner());
		assertNotEquals("The owner of Pit 6 should not be Player.PLAYER_2", Player.PLAYER_2, game.getBoard().getPits().get(6).getOwner());
	}
	
	@Test
	public void whenNewGameIsCreated_Player2_OnwsLargePit13() {
		Game game = GameFactory.newGame();
		Player expected = Player.PLAYER_2;
		assertEquals("The owner of Pit 13 should be Player.PLAYER_2", expected, game.getBoard().getPits().get(13).getOwner());
		assertNotEquals("The owner of Pit 6 should not be Player.PLAYER_1", Player.PLAYER_1, game.getBoard().getPits().get(13).getOwner());
	}
	
	@Test
	public void whenNewGameIsCreated_Pits_6_and_13_areLarge() {
		Game game = GameFactory.newGame();
		PitType expected = PitType.LARGE;
		assertEquals("The 6 should be PitType.LARGE", expected, game.getBoard().getPits().get(6).getType());
		assertNotEquals("The pit 6 should not be PitType.REGULAR", PitType.REGULAR, game.getBoard().getPits().get(6).getType());
		assertEquals("The 13 should be PitType.LARGE", expected, game.getBoard().getPits().get(13).getType());
		assertNotEquals("The owner of Pit 13 should not be PitType.REGULAR", PitType.REGULAR, game.getBoard().getPits().get(13).getType());
	}
}
