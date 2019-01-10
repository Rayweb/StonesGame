package com.bol.game.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bol.game.domain.Game;
import com.bol.game.domain.Turn;
import com.bol.game.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameService gameService;
	
	@Before
	public void setUp() {
		 JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	public void getboardHttpStatusOkTest() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	public void whenPathVariableIsInvalidPlayerId_thenReturnsStatus422() throws Exception {
		this.mockMvc.perform(post("/register/player/PLAYER_3")).andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void whenPathVariableIsInvalidPitID_thenReturnsStatus422() throws Exception {
		this.mockMvc.perform(post("/playTurn/PLAYER_2/14")).andExpect(status().isUnprocessableEntity());
	}
	
}
