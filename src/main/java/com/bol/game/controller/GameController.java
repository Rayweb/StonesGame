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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bol.game.domain.Game;
import com.bol.game.domain.Player;
import com.bol.game.service.GameService;

@Controller
public class GameController {

	@Autowired
	GameService gameService;

	private List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());

	@GetMapping("/")
	public String hello() {
		return "board";
	}

	@GetMapping("/game")
	public @ResponseBody Game getNewGame() {
		return gameService.getNewGame();
	}

	@GetMapping("/games")
	public @ResponseBody String getUser() {
		synchronized (this.sseEmitters) {
			for (SseEmitter sseEmitter : this.sseEmitters) {
				try {
					sseEmitter.send("hola");
				} catch (Exception e) {
				}
			}
		}
		return "Ok";
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

	@PostMapping("/register/{playerId}")
	public ResponseEntity<String> registerPlayer(@PathVariable String playerId) {

		if (playerId.equals(Player.PLAYER_1.toString()) || playerId.equals(Player.PLAYER_2.toString())) {
			if (playerId.equals(Player.PLAYER_1.toString())){
				gameService.getGame().setPlayer1Active(true);
			}else {
				gameService.getGame().setPlayer2Active(true);
			}
			synchronized (this.sseEmitters) {
				for (SseEmitter sseEmitter : this.sseEmitters) {
					try {
						sseEmitter.send(gameService.getGame());
					} catch (Exception e) {
					}
				}
			}
			return new ResponseEntity<>("You got it! lets Play " + playerId, HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid Player", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AsyncRequestTimeoutException.class)
	public String asyncTimeout(AsyncRequestTimeoutException e) {
		return null; // "SSE timeout..OK";
	}

}
