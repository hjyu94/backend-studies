package me.hjeong._9_data_binding.ConverterFormatter;

import org.springframework.core.convert.converter.Converter;

public class EventConverter {
    // "1" -> Event{id=1, ...}
    public static class StringToEventConverter implements Converter<String, Event> {
        @Override
        public Event convert(String s) {
            return new Event(Integer.parseInt(s));
        }
    }

    // Event -> String
    public static class EventToStringConverter implements Converter<Event, String> {
        @Override
        public String convert(Event event) {
            return event.toString();
        }
    }
}
