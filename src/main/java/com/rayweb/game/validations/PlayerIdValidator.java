package com.rayweb.game.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.rayweb.game.domain.Player;

class PlayerIdValidator implements ConstraintValidator<PlayerId, String> {
    @Override
    public void initialize(PlayerId playerId) {
    }
 
    @Override
    public boolean isValid(String playerId, ConstraintValidatorContext cxt) {
    	return playerId.equals(Player.PLAYER_1.toString()) || playerId.equals(Player.PLAYER_2.toString());
    }
}