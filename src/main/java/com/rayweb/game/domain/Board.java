package com.rayweb.game.domain;

import java.util.List;

import lombok.Data;

@Data
public class Board {

	private static final int BIG_PIT_PLAYER_1 = 6;
	private static final int BIG_PIT_PLAYER_2 = 13;

	public List<Pit> pits;

	public boolean isValidMove(Turn turn) {
		int pit = turn.getPit().getId();
		return isPLayersPit(turn) && pitHasStones(pit) && isRegularPit(pit);
	}

	public boolean pitHasStones(int pit) {
		return pits.get(pit).getStones() > 0;
	}

	public boolean isPLayersPit(Turn turn) {
		return turn.getPlayer() == pits.get(turn.pit.getId()).getOwner();
	}

	public boolean isRegularPit(int pit) {
		return pits.get(pit).getType() == PitType.REGULAR;
	}

	public boolean isOponentBigPit(Turn turn) {
		return !isRegularPit(turn.getPit().getId()) && !isPLayersPit(turn);
	}

	public void collectStonesInPits() {
		setTotalPlayerStones(Player.PLAYER_1, BIG_PIT_PLAYER_1);
		setTotalPlayerStones(Player.PLAYER_2, BIG_PIT_PLAYER_2);
	}

	private void setTotalPlayerStones(Player player, int bigPit) {
		int stonesInPitsPLayer = getStonesLeftforPlayer(player);
		int stonesInBigPit = pits.get(bigPit).getStones();
		stonesInBigPit = stonesInBigPit + stonesInPitsPLayer;
		pits.get(bigPit).setStones(stonesInBigPit);
	}

	private int getStonesLeftforPlayer(Player player) {
		int stones = 0;
		for (Pit pit : pits) {
			if ((pit.getOwner() == player) && pit.getType() == PitType.REGULAR) {

				stones = stones + takeStones(pit.getId());
			}
		}
		return stones;
	}

	public int takeStones(int pit) {
		int stonesInHand = pits.get(pit).getStones();
		pits.get(pit).setStones(0);
		return stonesInHand;
	}

	public void dropStone(Turn turn) {
		int pitId = turn.getPit().getId();
		int stones = pits.get(pitId).getStones();
		stones = stones + 1;
		pits.get(pitId).setStones(stones);
	}

	public void captureStones(Turn turn) {
		int capturedStones = takeStones(turn.getPit().getId());
		capturedStones = capturedStones + takeStones(turn.getPit().getOpositePit());
		if (turn.getPlayer() == Player.PLAYER_1) {
			addStonesToBitPit(capturedStones, BIG_PIT_PLAYER_1);
		} else {
			addStonesToBitPit(capturedStones, BIG_PIT_PLAYER_2);
		}
	}

	private void addStonesToBitPit(int capturedStones, int pitId) {
		int stonesInBigPit = pits.get(pitId).getStones();
		stonesInBigPit = stonesInBigPit + capturedStones;
		pits.get(pitId).setStones(stonesInBigPit);
	}

	public boolean canCapture(Turn turn) {
		int pitId = turn.getPit().getId();
		int stonesInPit = pits.get(pitId).getStones();
		return stonesInPit == 1 && isPLayersPit(turn) && isRegularPit(pitId);
	}

	public long getStonesOfPlayer(Player player) {
		return pits.stream().filter(pit -> pit.getType() == PitType.REGULAR).filter(pit -> pit.getOwner() == player)
				.mapToInt(Pit::getStones).summaryStatistics().getSum();
	}
}
