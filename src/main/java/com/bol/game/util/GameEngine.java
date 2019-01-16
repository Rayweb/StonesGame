package com.bol.game.util;

import org.springframework.stereotype.Component;

import com.bol.game.domain.Board;
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
	private Board board;
   
	public GameEngine() {
		game = GameFactory.newGame();
		board = game.getBoard();
	}

	public Game getGame() {
		return this.game;
	}
	
	public void resetGame() {
		game = GameFactory.newGame();
		board = game.getBoard();
		game.setState(GameState.RESTARTED);
	}

	public void playNextTurn(Turn turn) {
		if (board.isValidMove(turn)) {
			setNextTurn(turn.getPlayer());
			int stonesInHand = board.takeStones(turn.getPit().getId());
			turn = moveTurnToNextPit(turn);
			for (int i = stonesInHand; i > 0; i--) {
				board.dropStone(turn, i);
				if (i == 1) {
					setStateAfterLastDrop(turn);
				} else {
					turn = moveTurnToNextPit(turn);
				}
			}
		}
	}

	private void setStateAfterLastDrop(Turn turn) {
		if (board.canCapture(turn)) {
			board.captureStones(turn);
		}
		if (endOfGame()) {
			board.collectStonesInPits();
			game.setWinner(getWinner());
			game.setState(GameState.FINISHED);
		}
		if (playerGetsNewTurn(turn)) {
			game.setNextTurn(turn.getPlayer());
		}
	}

	private String getWinner() {
		String winner;
		int stonesPlayer1 = board.getPits().get(BIG_PIT_PLAYER_1).getStones();
		int stonesPlayer2 = board.getPits().get(BIG_PIT_PLAYER_2).getStones();
		
		if (stonesPlayer1 > stonesPlayer2) {
			winner = Player.PLAYER_1.toString();
		} else {
			winner = Player.PLAYER_2.toString();
		}
		
		if (stonesPlayer1 == stonesPlayer2) {
			winner = "DRAW";
		}
		
		return winner;
	}

	public boolean endOfGame() {
		long playerStones1 = board.getStonesOfPlayer(Player.PLAYER_1);
		long playerStones2 = board.getStonesOfPlayer(Player.PLAYER_2);
		return playerStones1 == 0 || playerStones2 == 0;
	}

	private boolean playerGetsNewTurn(Turn turn) {
		int pitId = turn.getPit().getId();
		return !board.isRegularPit(pitId) && board.isPLayersPit(turn);
	}

	public Turn moveTurnToNextPit(Turn turn) {
		int nextPit = turn.getPit().getId() + 1;
		if (nextPit == 14) {
			nextPit = 0;
		}
		turn.setPit(board.getPits().get(nextPit));
		if (board.isOponentBigPit(turn)) {
			if (turn.getPit().getId() == 13) {
				nextPit = 0;
			} else {
				nextPit = turn.getPit().getId() + 1;
			}
			turn.setPit(board.getPits().get(nextPit));
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
		return game.getState().equals(GameState.READY) || game.getState().equals(GameState.RESTARTED);
			
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
		if (playerId.equals(Player.PLAYER_1.toString())) {
			if (game.isPlayer1Active()) {
				throw new PlayerAlreadyActiveException("Player 1 is already Active");
			} else {
				game.setPlayer1Active(true);
			}
		} else if (playerId.equals(Player.PLAYER_2.toString())) {
			if (game.isPlayer2Active()) {
				throw new PlayerAlreadyActiveException("Player 2 is already Active");
			} else {
				game.setPlayer2Active(true);
			}
		}
	}

	public boolean isValidPlayerId(final String playerId) throws InvalidPlayerIdException {
		if (playerId.equals(Player.PLAYER_1.toString()) || playerId.equals(Player.PLAYER_2.toString())) {
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
