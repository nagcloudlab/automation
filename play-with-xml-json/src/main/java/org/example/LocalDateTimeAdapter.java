package org.example;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter
        extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime unmarshal(String value) {
        return LocalDateTime.parse(value, FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime value) {
        return value.format(FORMATTER);
    }
}
