package me.hjeong._9_data_binding.PropertyEditor;

import java.beans.PropertyEditorSupport;

public class EventEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Event event = (Event) getValue();
        return event.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Event event = new Event(Integer.parseInt(text));
        setValue(event);
    }
}
