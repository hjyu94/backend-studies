package me.hjeong._8_validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass); // Event 클래스인 경우 검증하겠다.
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "title", "notempty", "Empty title is not allowed");

        Event event = (Event) o;
        if (event.getTitle() == null) {
            errors.rejectValue("title", "notempty");
        }
    }
}
