package com.rayweb.game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.rayweb.game.domain.GameState;
import com.rayweb.game.domain.Player;
import com.rayweb.game.domain.Turn;
import com.rayweb.game.exception.GameStateException;
import com.rayweb.game.exception.InvalidPlayerIdException;
import com.rayweb.game.exception.PlayerAlreadyActiveException;
import com.rayweb.game.service.GameService;
import com.rayweb.game.validations.PlayerId;

@Controller
@Validated
public class GameController {

	private final Logger logger = LoggerFactory.getLogger(GameController.class);

	private final GameService gameService;
	
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	private final List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());

	@GetMapping("/")
	public String getboard() {
		return "board";

	}

	@PostMapping("/reset")
	public ResponseEntity<String> resetGame() {
		gameService.resetGame();
		if (gameService.getGame().getState().equals(GameState.RESTARTED)) {
			sentGame();
			logger.info("Game Event - Game Restarted");
			return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
		} else {
			return new ResponseEntity<>("Error restarting the game", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/register/player/{playerId}")
	public ResponseEntity<String> registerPlayer1(@PathVariable("playerId") @PlayerId String playerId)
			throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameService.registerPlayer(playerId);
		sentGame();
		logger.info("Game Event - Player : " + playerId + " Register");
		return new ResponseEntity<>("You joined as " + playerId, HttpStatus.OK);
	}

	@PostMapping("/playTurn/{playerId}/{pitId}")
	public ResponseEntity<String> playeNextTurn(@PathVariable("playerId") @PlayerId String playerId,
			@PathVariable("pitId") @Min(0) @Max(13) int pitId) {

		if (playerId.equals(Player.PLAYER_1.toString())) {
			Turn turn = new Turn(Player.PLAYER_1, gameService.getGame().getBoard().getPits().get(pitId));
			gameService.playNextTurn(turn);
		} else {
			Turn turn = new Turn(Player.PLAYER_2, gameService.getGame().getBoard().getPits().get(pitId));
			gameService.playNextTurn(turn);
		}
		sentGame();
		logger.info("Game Event - player : " + playerId + " moved stones in pit:" + pitId);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@RequestMapping("/sse")
	public SseEmitter getSseEmitter() {
		SseEmitter sseEmitter = new SseEmitter();
		synchronized (this.sseEmitters) {
			this.sseEmitters.add(sseEmitter);
			sseEmitter.onCompletion(() -> {
				synchronized (this.sseEmitters) {
					this.sseEmitters.remove(sseEmitter);
				}
			});
			sseEmitter.onTimeout(() -> {
				synchronized (this.sseEmitters) {
					this.sseEmitters.remove(sseEmitter);
				}
			});
		}
		return sseEmitter;
	}

	private void sentGame() {
		synchronized (this.sseEmitters) {
			for (SseEmitter sseEmitter : this.sseEmitters) {
				try {
					sseEmitter.send(gameService.getGame());
				} catch (Exception e) {
				}
			}
		}
	}

}
