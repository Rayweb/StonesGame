package com.bol.game.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bol.game.domain.Game;
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
				} catch (Exception e) {}
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
	
	@ExceptionHandler(value = AsyncRequestTimeoutException.class)  
    public String asyncTimeout(AsyncRequestTimeoutException e){  
        return null; // "SSE timeout..OK";  
    }
	
}
