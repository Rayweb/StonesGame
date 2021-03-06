package com.rayweb.game.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rayweb.game.domain.Player;
import com.rayweb.game.domain.Turn;
import com.rayweb.game.exception.GameStateException;
import com.rayweb.game.exception.InvalidPlayerIdException;
import com.rayweb.game.exception.PlayerAlreadyActiveException;
import com.rayweb.game.service.GameService;
import com.rayweb.game.util.GameEngine;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTests {

	@Autowired
	private GameService gameService;

	@MockBean
	private GameEngine gameEngine;
	
	@MockBean
	private Turn turn;

	@Before
	public void setupMock() {
		gameService = new GameService(gameEngine);
	}
	
	@Test
	public void resetGame_isCalled() {
		gameService.resetGame();
		verify(gameEngine, atLeastOnce()).resetGame();
	}
	
	@Test
	public void move_isCalled() {
		gameService.playNextTurn(turn);
		verify(gameEngine, atLeastOnce()).playNextTurn(turn);
	}
	
	@Test
	public void getGame_isCalled() {
		gameService.getGame();
		verify(gameEngine, atLeastOnce()).getGame();
	}
	
	@Test
	public void registerPlayerisCalled_isCalled() throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameService.registerPlayer(Player.PLAYER_1.toString());
		verify(gameEngine, atLeastOnce()).registerPlayer(Player.PLAYER_1.toString());
	}
}
