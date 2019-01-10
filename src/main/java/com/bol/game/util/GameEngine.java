package com.bol.game.util;

import org.springframework.stereotype.Component;

import com.bol.game.domain.Game;
import com.bol.game.domain.GameState;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;
import com.bol.game.exception.GameStateException;
import com.bol.game.exception.InvalidPlayerIdException;
import com.bol.game.exception.PlayerAlreadyActiveException;

@Component
public class GameEngine {

	private static final int BIG_PIT_PLAYER_1 = 6;
	private static final int BIG_PIT_PLAYER_2 = 13;

	private Game game;
   
	public GameEngine() {
		game = GameFactory.newGame();
	}

	public Game getGame() {
		return this.game;
	}
	
	public void resetGame() {
		game = GameFactory.newGame();
		game.setState(GameState.RESTARTED);
	}

	public void playNextTurn(Turn turn) {
		if (game.getBoard().isValidMove(turn)) {
			setNextTurn(turn.getPlayer());
			int stonesInHand = game.getBoard().takeStones(turn.getPit().getId());
			turn = moveTurnToNextPit(turn);
			for (int i = stonesInHand; i > 0; i--) {
				game.getBoard().dropStone(turn, i);
				if (i == 1) {
					setStateAfterLastDrop(turn);
				} else {
					turn = moveTurnToNextPit(turn);
				}
			}
		}
	}

	private void setStateAfterLastDrop(Turn turn) {
		if (game.getBoard().canCapture(turn)) {
			game.getBoard().captureStones(turn);
		}
		if (endOfGame()) {
			game.getBoard().collectStonesInPits();
			String winner = getWinner();
			game.setWinner(winner);
			game.setState(GameState.FINISHED);
		}
		if (playerGetsNewTurn(turn)) {
			game.setNextTurn(turn.getPlayer());
		}
	}

	private String getWinner() {
		int stonesPlayer1 = game.getBoard().getPits().get(BIG_PIT_PLAYER_1).getStones();
		int stonesPlayer2 = game.getBoard().getPits().get(BIG_PIT_PLAYER_2).getStones();
		if (stonesPlayer1 == stonesPlayer2) {
			return "DRAW";
		}
		if (stonesPlayer1 > stonesPlayer2) {
			return Player.PLAYER_1.toString();
		} else {
			return Player.PLAYER_2.toString();
		}
	}

	public boolean endOfGame() {
		long playerStones1 = game.getBoard().getStonesOfPlayer(Player.PLAYER_1);
		long playerStones2 = game.getBoard().getStonesOfPlayer(Player.PLAYER_2);
		if (playerStones1 == 0 || playerStones2 == 0) {
			return true;
		}
		return false;
	}

	private boolean playerGetsNewTurn(Turn turn) {
		int pitId = turn.getPit().getId();
		return !game.getBoard().isRegularPit(pitId) && game.getBoard().isPLayersPit(turn);
	}

	public Turn moveTurnToNextPit(Turn turn) {
		int nextPit = turn.getPit().getId() + 1;
		if (nextPit == 14) {
			nextPit = 0;
		}
		turn.setPit(game.getBoard().getPits().get(nextPit));
		if (game.getBoard().isOponentBigPit(turn)) {
			if (turn.getPit().getId() == 13) {
				nextPit = 0;
			} else {
				nextPit = turn.getPit().getId() + 1;
			}
			turn.setPit(game.getBoard().getPits().get(nextPit));
		}
		return turn;
	}

	public void setNextTurn(Player player) {
		if (player.equals(Player.PLAYER_1)) {
			this.game.setNextTurn(Player.PLAYER_2);
		} else {
			this.game.setNextTurn(Player.PLAYER_1);
		}
	}

	public boolean gameStateAllowsRegistration() {
		if (game.getState().equals(GameState.READY) || game.getState().equals(GameState.RESTARTED)) {
			return true;
		}
		return false;
	}

	public void registerPlayer(String playerId)
			throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		if (!gameStateAllowsRegistration()) {
			throw new GameStateException(
					"The current Game state: " + game.getState().toString() + " does not allow registration");
		}
		if (isValidPlayerId(playerId)) {
			setPlayerStatus(playerId);
			setGameStateAfterRegistration();
		}
	}

	private void setPlayerStatus(String playerId) throws PlayerAlreadyActiveException, InvalidPlayerIdException {
		if (playerId.equals("PLAYER_1")) {
			if (game.isPlayer1Active()) {
				throw new PlayerAlreadyActiveException("Player 1 is already Active");
			} else {
				game.setPlayer1Active(true);
			}
		} else if (playerId.equals("PLAYER_2")) {
			if (game.isPlayer2Active()) {
				throw new PlayerAlreadyActiveException("Player 2 is already Active");
			} else {
				game.setPlayer2Active(true);
			}
		}
	}

	public boolean isValidPlayerId(String playerId) throws InvalidPlayerIdException {
		if (playerId.equals("PLAYER_1") || playerId.equals("PLAYER_2")) {
			return true;
		} else {
			throw new InvalidPlayerIdException("Invalid Player Id");
		}
	}

	public void setGameStateAfterRegistration() {
		if (game.isPlayer1Active() && game.isPlayer2Active()) {
			game.setState(GameState.STARTED);
		} else if (game.isPlayer1Active() || game.isPlayer2Active()) {
			game.setState(GameState.READY);
		}
	}
}
