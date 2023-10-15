package com.blackcompany.eeos.program.presentation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = OverDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverDate {

	String message() default "지난 날짜는 생성하지 못합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
