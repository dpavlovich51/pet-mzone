package com.mzone.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;

//// TODO: 08/05/2022 investigate event way with BaseEvent
@Getter
@AllArgsConstructor
public class BaseEvent {

    private final UUID id;
    private final UUID sourceId;
    private final UUID userId;
    private final DateTime created;

    public static BaseEvent createSourceEvent(UUID userId) {
        return new BaseEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                userId,
                DateTime.now(DateTimeZone.UTC)
        );
    }
}
