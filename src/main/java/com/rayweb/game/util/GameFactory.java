package com.rayweb.game.util;

import java.util.ArrayList;
import java.util.List;

import com.rayweb.game.domain.Board;
import com.rayweb.game.domain.Game;
import com.rayweb.game.domain.GameState;
import com.rayweb.game.domain.Pit;
import com.rayweb.game.domain.PitType;
import com.rayweb.game.domain.Player;

public class GameFactory {

	private static final Integer INITIAL_STONES = 6;

	public static Game newGame() {
		Game newGame = new Game();
		newGame.setState(GameState.READY);
		newGame.setPlayer1Active(false);
		newGame.setPlayer2Active(false);
		newGame.setNextTurn(Player.PLAYER_1);
		newGame.setBoard(setupBoard());
		return newGame;
	}

	public static Board setupBoard() {
		Board board = new Board();
		List<Pit> pits = setupPlayer1Pits();
		pits.addAll(setupPlayer2Pits());
		board.setPits(pits);
		return board;
	}

	public static List<Pit> setupPlayer1Pits() {
		List<Pit> player1Pits = new ArrayList<Pit>();
		player1Pits.add(new Pit(0, Player.PLAYER_1, 12, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(1, Player.PLAYER_1, 11, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(2, Player.PLAYER_1, 10, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(3, Player.PLAYER_1, 9, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(4, Player.PLAYER_1, 8, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(5, Player.PLAYER_1, 7, PitType.REGULAR, INITIAL_STONES));
		player1Pits.add(new Pit(6, Player.PLAYER_1, 13, PitType.LARGE, 0));
		return player1Pits;
	}

	public static List<Pit> setupPlayer2Pits() {
		List<Pit> player2Pits = new ArrayList<Pit>();
		player2Pits.add(new Pit(7, Player.PLAYER_2, 5, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(8, Player.PLAYER_2, 4, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(9, Player.PLAYER_2, 3, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(10, Player.PLAYER_2, 2, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(11, Player.PLAYER_2, 1, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(12, Player.PLAYER_2, 0, PitType.REGULAR, INITIAL_STONES));
		player2Pits.add(new Pit(13, Player.PLAYER_2, 6, PitType.LARGE, 0));
		return player2Pits;
	}
}
