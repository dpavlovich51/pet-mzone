package com.mzone.main.core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class UTime {
    public static DateTime timeNow() {
        return DateTime.now(DateTimeZone.UTC);
    }
}
