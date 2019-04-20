package com.n26.fixtures;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFixtures {

    public static final ZonedDateTime ZONED_DATE_TIME_NOW = ZonedDateTime.now(ZoneId.of("UTC"));
    public static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    public static final String NOW = ZONED_DATE_TIME_NOW.format(ISO_FORMAT);
    public static final String TWO_MINUTES_AGO = ZONED_DATE_TIME_NOW.minusMinutes(2).format(ISO_FORMAT);
    public static final String TOMORROW = ZONED_DATE_TIME_NOW.plusDays(1).format(ISO_FORMAT);
}
