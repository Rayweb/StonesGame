package com.bol.game.util;

import com.bol.game.domain.Game;
import com.bol.game.domain.GameState;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;

public class GameEngine {

	private Game game;

	public GameEngine() {
		game = GameFactory.newGame();
	}

	public void playNextTurn(Turn turn) {
		if (isValidMove(turn)) {
			setNextTurn(turn.getPlayer());
			int stonesInHand = takeStones(turn.getPit().getId());
			turn = moveTurnToNextPit(turn);
			for (int i = stonesInHand; i > 0; i--) {
				dropStone(turn.getPit().getId());
				if (i == 1) {
					setStateAfterLastDrop(turn);
				} else {
					turn = moveTurnToNextPit(turn);
				}
			}
		}

	}

	private void setStateAfterLastDrop(Turn turn) {
		if (ifEndOfGame()) {
			game.setState(GameState.FINISHED);
		}
		if (checkIfNewTurn(turn)) {
			game.setNextTurn(turn.getPlayer());
		}
	}

	private boolean ifEndOfGame() {
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

	private boolean checkIfNewTurn(Turn turn) {
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

	public void dropStone(int pit) {
		int stones = game.board.getPits().get(pit).getStones();
		stones = stones + 1;
		game.board.getPits().get(pit).setStones(stones);
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
}
