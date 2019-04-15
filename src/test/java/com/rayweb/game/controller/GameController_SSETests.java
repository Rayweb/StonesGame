package com.rayweb.game.controller;
//package com.rayweb.game.controller;
//
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import com.rayweb.game.domain.Game;
//import com.rayweb.game.domain.GameState;
//import com.rayweb.game.service.GameService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(GameController.class)
//public class GameController_SSETests {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private GameService gameService;
//
//	@MockBean
//	private Game game;
//
//	@MockBean
//	private List<SseEmitter> sseEmitters;
//
//	@Before
//	public void setUp() {
//		JacksonTester.initFields(this, new ObjectMapper());
//	}
//
//	@Test
//	public void resetGame_thenResult() throws Exception {
//		doAnswer(doNothing()).when(gameService).resetGame();
//		when(gameService.getGame().getState()).thenReturn(GameState.RESTARTED);
//		this.mockMvc.perform(post("/reset")).andExpect(status().isOk());
//	}
//
//	@Test
//	public void resetGame_then() throws Exception {
//		doAnswer(doNothing()).when(gameService).registerPlayer("PLAYER_1");
//		this.mockMvc.perform(post("/playTurn/PLAYER_1/2")).andExpect(status().isOk());
//	}
//
//	public Answer<Void> doNothing() {
//		return new Answer<Void>() {
//			public Void answer(InvocationOnMock invocation) {
//				return null;
//			}
//		};
//	}
//}
