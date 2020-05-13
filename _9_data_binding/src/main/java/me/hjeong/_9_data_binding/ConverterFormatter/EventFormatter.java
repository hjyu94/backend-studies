package me.hjeong._9_data_binding.ConverterFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class EventFormatter implements Formatter<Event> {
    // "1" -> Event{id=1, ...}
    @Override
    public Event parse(String s, Locale locale) throws ParseException {
        Event event = new Event();
        int id = Integer.parseInt(s);
        event.setId(id);
        return event;
    }

    // Event{id=1, ...} -> "1"
    @Override
    public String print(Event event, Locale locale) {
        return event.getId().toString();
    }
}
