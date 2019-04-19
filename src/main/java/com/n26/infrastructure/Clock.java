package com.n26.infrastructure;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class Clock {

    public ZonedDateTime now() {
        return  ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
