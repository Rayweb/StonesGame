package com.rayweb.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Turn {

	public Player player;
	public Pit pit;	
}
