package com.mzone.main.events.value;

import ch.hsr.geohash.GeoHash;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationValue {

    private final BigDecimal lat;
    private final BigDecimal lng;

    private LocationValue(BigDecimal lat, BigDecimal lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static LocationValue create(BigDecimal lat, BigDecimal lng) {
        return new LocationValue(lat, lng);
    }

    public String toGeoHash() {
        return GeoHash.withCharacterPrecision(
                getLat().doubleValue(),
                getLng().doubleValue(),
                5
        ).toBase32();
    }
}
