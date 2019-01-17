package com.bol.game.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bol.game.domain.Player;

public class PlayerIdValidator implements ConstraintValidator<PlayerId, String> {
    @Override
    public void initialize(PlayerId playerId) {
    }
 
    @Override
    public boolean isValid(String playerId, ConstraintValidatorContext cxt) {
    	return playerId.equals(Player.PLAYER_1.toString()) || playerId.equals(Player.PLAYER_2.toString());
    }
}