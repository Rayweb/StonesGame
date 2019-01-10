package com.bol.game.validations;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Documented
@Constraint(validatedBy = PlayerIdValidator.class)
@Target( { ElementType.PARAMETER,ElementType.FIELD, ElementType.TYPE_PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerId {
    String message() default "Invalid Player Id. It can Only Be PLAYER_1 or PLAYER_2";
    Class[] groups() default {};
    Class[] payload() default {};
}