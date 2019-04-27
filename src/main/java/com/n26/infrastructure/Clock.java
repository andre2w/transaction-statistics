package com.n26.infrastructure;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Clock {

    public static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));

    public ZonedDateTime now() {
        return  ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public ZonedDateTime parse(String dateTime) {
        return ZonedDateTime.parse(dateTime, ISO_FORMAT);
    }
}
