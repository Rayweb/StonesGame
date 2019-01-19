package com.rayweb.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pit {
	
	public Integer id;
	public Player owner;
	public Integer opositePit;
	public PitType type;
	public Integer stones;
	
}
