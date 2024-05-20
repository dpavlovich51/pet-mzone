package com.mzone.main.events;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FindEventModel {
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public FindEventModel(BigDecimal latitude,
                          BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
