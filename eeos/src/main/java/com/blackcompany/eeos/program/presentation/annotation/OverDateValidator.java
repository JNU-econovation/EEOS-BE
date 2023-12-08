package com.blackcompany.eeos.program.presentation.annotation;

import com.blackcompany.eeos.common.utils.DateConverter;
import java.sql.Timestamp;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class OverDateValidator implements ConstraintValidator<OverDate, Timestamp> {

	@Override
	public void initialize(OverDate constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Timestamp value, ConstraintValidatorContext context) {
		LocalDate now = LocalDate.now();
		LocalDate requestDate = DateConverter.toLocalDate(value.getTime());

		return now.isBefore(requestDate) || now.isEqual(requestDate);
	}
}
