package com.blackcompany.eeos.program.presentation.annotation;

import com.blackcompany.eeos.common.support.converter.DateConverter;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class OverDateValidator implements ConstraintValidator<OverDate, String> {

	@Override
	public void initialize(OverDate constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Objects.requireNonNull(value);

		LocalDate now = DateConverter.toLocalDate(System.currentTimeMillis());
		LocalDate requestDate = DateConverter.toLocalDate(Long.valueOf(value));

		if (now.isBefore(requestDate)) {
			return false;
		}
		return true;
	}
}
