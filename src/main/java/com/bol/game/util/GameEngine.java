package com.bol.game.util;

import org.springframework.stereotype.Component;

import com.bol.game.domain.Game;
import com.bol.game.domain.GameState;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;
import com.bol.game.util.exception.GameStateException;
import com.bol.game.util.exception.InvalidPlayerIdException;
import com.bol.game.util.exception.PlayerAlreadyActiveException;

@Component
public class GameEngine {

	private Game game;

	public GameEngine() {
		game = GameFactory.newGame();
	}

	public void resetGame() {
		game = GameFactory.newGame();
		game.setState(GameState.RESTARTED);
	}

	public void playNextTurn(Turn turn) {
		if (isValidMove(turn)) {
			setNextTurn(turn.getPlayer());
			int stonesInHand = takeStones(turn.getPit().getId());
			turn = moveTurnToNextPit(turn);
			for (int i = stonesInHand; i > 0; i--) {
				dropStone(turn, i);
			}
		}
	}

	private void setStateAfterLastDrop(Turn turn) {
		if (canCapture(turn)) {
			captureStones(turn);
		}
		if (endOfGame()) {
			String winner = getWinner();
			game.setWinner(winner);
			game.setState(GameState.FINISHED);
		}
		if (playerGetsNewTurn(turn)) {
			game.setNextTurn(turn.getPlayer());
		}
	}

	private String getWinner() {
		int stonesPlayer1 = game.getBoard().getPits().get(6).getStones();
		int stonesPlayer2 = game.getBoard().getPits().get(13).getStones();
		if (stonesPlayer1 == stonesPlayer2) {
			return "DRAW";
		}
		if (stonesPlayer1 > stonesPlayer2) {
			return Player.PLAYER_1.toString();
		} else {
			return Player.PLAYER_2.toString();
		}
	}

	public boolean canCapture(Turn turn) {
		int pitId = turn.getPit().getId();
		int stonesInPit = game.getBoard().getPits().get(pitId).getStones();
		if (stonesInPit == 1 && isPLayersPit(turn) && isRegularPit(pitId)) {
			return true;
		}
		return false;
	}

	public void captureStones(Turn turn) {
		int capturedStones = takeStones(turn.getPit().getId());
		capturedStones = capturedStones + takeStones(turn.getPit().getOpositePit());
		if (turn.getPlayer() == Player.PLAYER_1) {
			int stonesInBigPit = game.getBoard().getPits().get(6).getStones();
			stonesInBigPit = stonesInBigPit + capturedStones;
			game.getBoard().getPits().get(6).setStones(stonesInBigPit);
		} else {
			int stonesInBigPit = game.getBoard().getPits().get(13).getStones();
			stonesInBigPit = stonesInBigPit + capturedStones;
			game.getBoard().getPits().get(13).setStones(stonesInBigPit);
		}
	}

	public boolean endOfGame() {
		long playerStones1 = getStonesOfPlayer(Player.PLAYER_1);
		long playerStones2 = getStonesOfPlayer(Player.PLAYER_2);
		if (playerStones1 == 0 || playerStones2 == 0) {
			return true;
		}
		return false;
	}

	private long getStonesOfPlayer(Player player) {
		return game.getBoard().getPits().stream()
				.filter(pit -> pit.getType() == PitType.REGULAR)
				.filter(pit -> pit.getOwner() == player)
				.mapToInt(pit -> pit.getStones())
				.summaryStatistics().getSum();
	}

	private boolean playerGetsNewTurn(Turn turn) {
		int pitId = turn.getPit().getId();
		if (!isRegularPit(pitId) && isPLayersPit(turn)) {
			return true;
		}
		return false;
	}

	public Turn moveTurnToNextPit(Turn turn) {
		int nextPit = turn.getPit().getId() + 1;
		if (nextPit == 14) {
			nextPit = 0;
		}
		turn.setPit(game.getBoard().getPits().get(nextPit));
		if (isOponentBigPit(turn)) {
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

	public Game getGame() {
		return this.game;
	}

	public int takeStones(int pit) {
		int stonesInHand = game.board.getPits().get(pit).getStones();
		game.board.getPits().get(pit).setStones(0);
		return stonesInHand;
	}

	public void dropStone(Turn turn, int remainingStones) {
		int pitId = turn.getPit().getId();
		int stones = game.board.getPits().get(pitId).getStones();
		stones = stones + 1;
		game.board.getPits().get(pitId).setStones(stones);
		if (remainingStones == 1) {
			setStateAfterLastDrop(turn);
		} else {
			turn = moveTurnToNextPit(turn);
		}
	}

	public boolean isValidMove(Turn turn) {
		int pit = turn.getPit().getId();
		if (isPLayersPit(turn) && pitHasStones(pit) && isRegularPit(pit)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean pitHasStones(int pit) {
		return game.getBoard().getPits().get(pit).getStones() > 0;
	}

	public boolean isPLayersPit(Turn turn) {
		if (turn.getPlayer() == game.getBoard().getPits().get(turn.pit.getId()).getOwner()) {
			return true;
		}
		return false;
	}

	public boolean isRegularPit(int pit) {
		if (game.getBoard().getPits().get(pit).getType() == PitType.REGULAR) {
			return true;
		}
		return false;
	}

	public boolean isOponentBigPit(Turn turn) {
		if (!isRegularPit(turn.getPit().getId()) && !isPLayersPit(turn)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean gameStateAllowsRegistration() {
		if (game.getState().equals(GameState.READY) || game.getState().equals(GameState.RESTARTED)) {
			return true;
		}
		return false;
	}

	public void registerPlayer(String playerId) throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException{
		if (!gameStateAllowsRegistration()) {
			throw new GameStateException("The current Game state: " + game.getState().toString() + " does not allow registration");
		}
		if(isValidPlayerId(playerId)) {
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
			}else {
				game.setPlayer2Active(true);
			}
		} 
	}
	
	public boolean isValidPlayerId(String playerId) throws InvalidPlayerIdException {
		if (playerId.equals("PLAYER_1") || playerId.equals("PLAYER_2")) {
			return true;
		}else {
			throw new InvalidPlayerIdException("Invalid Player Id");
		}
	}
	
	public void setGameStateAfterRegistration() {
		if(game.isPlayer1Active() && game.isPlayer2Active()) {
			game.setState(GameState.STARTED);
		}else if (game.isPlayer1Active() ||  game.isPlayer2Active()) {
			game.setState(GameState.READY);
		}
	}
}
