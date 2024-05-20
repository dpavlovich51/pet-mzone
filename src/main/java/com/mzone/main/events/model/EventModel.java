package com.mzone.main.events.model;

import com.mzone.main.events.data.Event;
import com.mzone.main.events.value.LocationValue;
import com.mzone.main.user.model.UserModel;
import lombok.Data;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class EventModel {

    private final UUID id;
    private final String name;
    private final Set<Integer> categories;
    private final Integer availability;
    private final String description;
    private final LocationValue location;
    private final UserModel owner;

    private final UUID titleImage;
    private final Collection<UUID> storyImages;

    private final ParticipantInfo participantInfo;

    private final DateTime startDate;
    private final DateTime endDate;
    private final DateTime createdAt;
    private final DateTime updatedAt;

    private EventModel(UUID id,
                       String name,
                       Set<Integer> categories,
                       Integer availability,
                       String description,
                       LocationValue location,
                       UserModel owner,
                       UUID titleImage,
                       Collection<UUID> storyImages,
                       ParticipantInfo participantInfo,
                       DateTime startDate,
                       DateTime endDate,
                       DateTime createdAt,
                       DateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.availability = availability;
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.titleImage = titleImage;
        this.storyImages = storyImages;
        this.participantInfo = participantInfo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EventModel create(Event event,
                                    UserModel owner,
                                    @Nullable UUID titleImage,
                                    Collection<UUID> storyImages,
                                    ParticipantInfo participantModel) {
        return new EventModel(
                event.getId(),
                event.getName(),
                event.getCategories().stream()
                        .map(Enum::ordinal)
                        .collect(Collectors.toSet()),
                event.getAvailability(),
                event.getDescription(),
                event.getLocation(),
                owner,
                titleImage,
                storyImages,
                participantModel,
                event.getStartDate(),
                event.getEndDate(),
                event.getCreatedAt(),
                event.getUpdatedAt()
                );
    }

    @Data
    public static class ParticipantInfo {

        private final Integer totalCount;
        private final Collection<ParticipantShortInfo> topParticipants;

        private ParticipantInfo(Integer totalCount,
                                Collection<ParticipantShortInfo> topParticipants) {
            this.totalCount = totalCount;
            this.topParticipants = topParticipants;
        }

        public static ParticipantInfo empty() {
            return new ParticipantInfo(
                    1,
                    List.of(new ParticipantShortInfo(UUID.randomUUID(),
                            UUID.randomUUID()))
            );
        }

        @Data
        public static class ParticipantShortInfo {
            private final UUID id;
            private final UUID avatar;
        }
    }

}
