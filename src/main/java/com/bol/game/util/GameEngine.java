package com.bol.game.util;

import com.bol.game.domain.Game;
import com.bol.game.domain.PitType;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;

public class GameEngine {

    private Game game;
    private int hand;
    
	public GameEngine() {
		game = GameFactory.newGame();
	}
	

	public void playNextTurn(Turn turn) {
		if(isValidMove(turn)){
			setNextTurn(turn.getPlayer());
			takeStones(turn.getPit().getId());
			turn = moveTurnToNextPit(turn);
			for(int i = hand;i >0;i--) {
					dropStone(turn.getPit().getId());
					turn = moveTurnToNextPit(turn);
			}
		}
		
	}
	
	public Turn moveTurnToNextPit(Turn turn) {
		int nextPit = turn.getPit().getId() +1;
		if(nextPit == 14) {
			nextPit = 0;
		}
		turn.setPit(game.getBoard().getPits().get(nextPit));
		if(isOponentBigPit(turn)) {
			if(turn.getPit().getId() == 13) {
				nextPit = 0;
			}else {
				nextPit = turn.getPit().getId() + 1;
			}
			turn.setPit(game.getBoard().getPits().get(nextPit));
		}
		return turn;
	}
	
	public void setNextTurn(Player player) {
		if(player.equals(Player.PLAYER_1)) {
			this.game.setNextTurn(Player.PLAYER_2);
		}else {
			this.game.setNextTurn(Player.PLAYER_1);
		}
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public void takeStones(int pit) {
		this.hand = game.board.getPits().get(pit).getStones();
		game.board.getPits().get(pit).setStones(0);
	}
	
	public void dropStone(int pit) {
		int stones = game.board.getPits().get(pit).getStones();
		stones = stones + 1;
		game.board.getPits().get(pit).setStones(stones);
	}
	
	public boolean isValidMove(Turn turn) {
		int pit = turn.getPit().getId();
		if(isPLayersPit(turn) && pitHasStones(pit) && isRegularPit(pit)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean pitHasStones(int pit) {
		return game.getBoard().getPits().get(pit).getStones() > 0;
	}


	public boolean isPLayersPit(Turn turn) {
		if(turn.getPlayer() == game.getBoard().getPits().get(turn.pit.getId()).getOwner()) {
			return true;
		}
		return false;
	}
	
	public boolean isRegularPit(int pit) {
		if(game.getBoard().getPits().get(pit).getType() == PitType.REGULAR) {
			return true;
		}
		return false;
	}
	
	public boolean isOponentBigPit(Turn turn) {
		if(!isRegularPit(turn.getPit().getId()) && !isPLayersPit(turn)) {
			return true;
		}else {
			return false;
		}
	}
}
