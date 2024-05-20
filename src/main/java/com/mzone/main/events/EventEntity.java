package com.mzone.main.events;

import com.mzone.main.core.BaseEntity;
import com.mzone.main.events.event.EventCategory;
import com.mzone.main.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = EventEntity.TABLE_NAME)
public class EventEntity extends BaseEntity {

    public static final String TABLE_NAME = "events";
    public static final String GEO_HASH_FIELD = "geoHash";

    @Column(name = "name")
    //    event entity
    private String name;
    @Column(name = "categories", nullable = false)
    @ElementCollection(targetClass = EventCategory.class, fetch = FetchType.EAGER)
    @JoinTable(name = "event_category", joinColumns = @JoinColumn(name = "event_id"))
    @Enumerated(EnumType.ORDINAL)
    private Set<EventCategory> categories;
    @Column(name = "availability")
    private Integer availability;
    @Column(name = "description")
    private String description;

    //    position entity
    @Column(name = "latitude")
    private BigDecimal latitude;
    @Column(name = "longitude")
    private BigDecimal longitude;
    @Column(name = GEO_HASH_FIELD)
    private String geoHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "start_date")
    private DateTime startDate;
    @Column(name = "end_date")
    private DateTime endDate;

    public EventEntity(UUID id,
                       String name,
                       Set<EventCategory> categories,
                       Integer availability,
                       String description,
                       BigDecimal latitude,
                       BigDecimal longitude,
                       String geoHash,
                       UserEntity user,
                       DateTime startDate,
                       DateTime endDate) {
        super(id);
        this.name = name;
        this.categories = categories;
        this.availability = availability;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = geoHash;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static EventEntity create(String name,
                                     Set<EventCategory> category,
                                     Integer availability,
                                     String description,
                                     BigDecimal latitude,
                                     BigDecimal longitude,
                                     String geoHash,
                                     UserEntity user,
                                     DateTime startDate,
                                     DateTime endDate) {
        return new EventEntity(
                UUID.randomUUID(),
                name,
                category,
                availability,
                description,
                latitude,
                longitude,
                geoHash,
                user,
                startDate,
                endDate
        );
    }

    public Set<Integer> getCategoryOrdinals() {
        return categories.stream()
                .map(Enum::ordinal)
                .collect(Collectors.toSet());
    }
}
