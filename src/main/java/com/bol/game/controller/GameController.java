package com.bol.game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bol.game.domain.GameState;
import com.bol.game.domain.Player;
import com.bol.game.domain.Turn;
import com.bol.game.exception.GameStateException;
import com.bol.game.exception.InvalidPlayerIdException;
import com.bol.game.exception.PlayerAlreadyActiveException;
import com.bol.game.service.GameService;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	private List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());

	@GetMapping("/")
	public String getboard() {
		return "board";

	}

	@PostMapping("/reset")
	public ResponseEntity<String> resetGame() {
		gameService.resetGame();
		if (gameService.getGame().getState().equals(GameState.RESTARTED)) {
			sentGame();
			return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
		}else {
			return new ResponseEntity<>("Error restarting the game", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/register/player/{playerId}")
	public ResponseEntity<String> registerPlayer1(@PathVariable("playerId") String playerId) throws PlayerAlreadyActiveException, InvalidPlayerIdException, GameStateException {
		gameService.registerPlayer(playerId);
		sentGame();
		return new ResponseEntity<>("You joined as " + playerId, HttpStatus.OK);
	}

	@PostMapping("/playTurn/{playerId}/{pitId}")
	public ResponseEntity<String> playeNextTurn(@PathVariable("playerId") String playerId,
			@PathVariable("pitId") int pitId) {
		if (playerId.equals(Player.PLAYER_1.toString()) || playerId.equals(Player.PLAYER_2.toString())) {
			if (playerId.equals(Player.PLAYER_1.toString())) {
				Turn turn = new Turn(Player.PLAYER_1, gameService.getGame().getBoard().getPits().get(pitId));
				gameService.playNextTurn(turn);
			} else {
				Turn turn = new Turn(Player.PLAYER_2, gameService.getGame().getBoard().getPits().get(pitId));
				gameService.playNextTurn(turn);
			}
			sentGame();
			return new ResponseEntity<>("OK", HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid move", HttpStatus.BAD_REQUEST);
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

	public void sentGame() {
		synchronized (this.sseEmitters) {
			for (SseEmitter sseEmitter : this.sseEmitters) {
				try {
					sseEmitter.send(gameService.getGame());
				} catch (Exception e) {
				}
			}
		}
	}

	@ExceptionHandler(value = AsyncRequestTimeoutException.class)
	public String asyncTimeout(AsyncRequestTimeoutException e) {
		return null; // "SSE timeout..OK";
	}

}
