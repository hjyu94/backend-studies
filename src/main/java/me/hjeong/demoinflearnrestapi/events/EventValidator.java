package me.hjeong.demoinflearnrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice())
        {
            if(eventDto.getMaxPrice() != 0)
            {
                errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong");
                errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
            }
            else
            {
                // 무제한 경매같은 케이스
            }
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()))
        {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDatetime
    }
}