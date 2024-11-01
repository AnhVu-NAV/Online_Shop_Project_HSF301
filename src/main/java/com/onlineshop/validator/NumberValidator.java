package com.onlineshop.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumberValidator implements ConstraintValidator<ValidNumber, String> {
    private int min;
    private int max;
    private String message;
    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String inputNumber, ConstraintValidatorContext validatorContext) {
        if (inputNumber == null) {
            return true;
        }
        try {
            // Sử dụng ResourceBundle để lấy thông báo lỗi
            if (message != null && message.contains("{")) {
                validatorContext.disableDefaultConstraintViolation();
                validatorContext.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }
            int number = Integer.parseInt(inputNumber);
            return number >= min && number <= max;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
