package com.mzone.main.events;

import com.mzone.main.core.BaseEntity;
import com.mzone.main.core.DomainEvent;
import com.mzone.main.events.data.Event;
import com.mzone.main.events.event.EventCategory;
import com.mzone.main.events.event.EventTitleImageChangedEvent;
import com.mzone.main.events.event.NewEventCreatedEvent;
import com.mzone.main.events.event.UserAddedEventStoryImageEvent;
import com.mzone.main.events.event.UserChangedEventTitleImageEvent;
import com.mzone.main.events.event.UserCreatedNewEventEvent;
import com.mzone.main.events.model.EventModel;
import com.mzone.main.events.policy.AddStoryImageToEventPolicy;
import com.mzone.main.events.policy.ChangeEventTitleImagePolicy;
import com.mzone.main.events.policy.EventCreatingPolicy;
import com.mzone.main.events.validator.EventCategoryConstraint;
import com.mzone.main.events.value.LocationValue;
import com.mzone.main.media.entity.ImageEntity;
import com.mzone.main.media.event.EventStoryImageAddedEvent;
import com.mzone.main.media.service.MediaService;
import com.mzone.main.user.entity.UserEntity;
import com.mzone.main.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/events")
public class EventController {

    private final EventCreatingPolicy eventCreatingPolicy;
    private final ChangeEventTitleImagePolicy changeEventTitleImagePolicy;
    private final AddStoryImageToEventPolicy addStoryImageToEventPolicy;
    private final MediaService mediaService;
    private final EventService eventService;

    public EventController(EventCreatingPolicy eventCreatingPolicy,
                           ChangeEventTitleImagePolicy changeEventTitleImagePolicy,
                           AddStoryImageToEventPolicy addStoryImageToEventPolicy,
                           MediaService mediaService,
                           EventService eventService) {
        this.eventCreatingPolicy = eventCreatingPolicy;
        this.changeEventTitleImagePolicy = changeEventTitleImagePolicy;
        this.addStoryImageToEventPolicy = addStoryImageToEventPolicy;
        this.mediaService = mediaService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public List<EventModel> getEvents(@RequestParam Double latitude,
                                    @RequestParam Double longitude) {
        return eventService
                .retrieve(EventUtils.toFindEventRequest(
                        BigDecimal.valueOf(latitude),
                        BigDecimal.valueOf(longitude)))
                .stream()
                .map(it -> {
                    final UserEntity user = it.getEvent().getUser();
                    return EventModel.create(
                            Event.createFromEntity(it.getEvent()),
                            UserModel.create(user, 0, null),
                            it.getTitleImage().map(BaseEntity::getId).orElse(null),
                            Collections.emptySet(),
                            EventModel.ParticipantInfo.empty()
                    );
                }).collect(Collectors.toList());
    }

    //// TODO: 08/06/2022 refactor it - create projections instead of dto
    @GetMapping("/{id}")
    public EventModel getEvent(@PathVariable UUID id) {
        final MediaEventEntity entity = eventService.retrieveEventWithMedia(id)
                .orElseThrow();

        final MediaEventResponseDto.MediaDto.Builder mediaBuilder = MediaEventResponseDto
                .MediaDto
                .newBuilder()
                .setTitleImage(entity.getTitleImage()
                        .map(ImageEntity::getId)
                        .orElse(null));

        entity.getStoryImages()
                .forEach(it -> mediaBuilder.addStoryImage(it.getId()));

        final UserEntity userEntity = entity.getEvent().getUser();
        return EventModel.create(
                Event.createFromEntity(entity.getEvent()),
                UserModel.create(userEntity, 0, null),
                entity.getTitleImage().map(BaseEntity::getId).orElse(null),
                Collections.emptySet(),
                EventModel.ParticipantInfo.empty()
        );
    }

    //// TODO: 02/06/2022 check image extension - allow only one type of extension
    //// TODO: 09/06/2022 separate it to 3 different APIs which each of them has only one policy
    @PostMapping("/new")
    public EventModel createEvent(@RequestPart("data") @Valid CreateEventDto dto,
                                  @RequestPart(value = "titleImage", required = false)
                                             MultipartFile titleImage,
                                  @Size(max = 5)
                                             @RequestPart(value = "storyImages", required = false)
                                             Collection<MultipartFile> storyImages,
                                  Authentication authentication) {
        final UserEntity user = (UserEntity) authentication.getPrincipal();

        final LocationValue locationValue = LocationValue.create(
                dto.getLocation().getLat(),
                dto.getLocation().getLng());

        final Set<EventCategory> categories = dto.getCategories()
                .stream()
                .map(it -> EventCategory.VALUES[it])
                .collect(Collectors.toSet());

        final NewEventCreatedEvent newEventCreatedEvent = eventCreatingPolicy
                .tryExecute(UserCreatedNewEventEvent.create(
                        user,
                        dto.getName(),
                        dto.getDescription(),
                        categories,
                        dto.getAvailability(),
                        locationValue,
                        dto.getStartTime(),
                        dto.getEndTime()
                ));

        final MediaEventResponseDto.MediaDto.Builder mediaBuilder = MediaEventResponseDto
                .MediaDto
                .newBuilder();

        Optional.ofNullable(titleImage)
                .ifPresent(it -> {
                    final EventTitleImageChangedEvent response = storeImageAndExecutePolicy(titleImage,
                            (storedImage) -> changeEventTitleImagePolicy.tryExecute(UserChangedEventTitleImageEvent
                                    .create(newEventCreatedEvent.getEvent().getId(), storedImage)));
                    mediaBuilder.setTitleImage(response.getTitleImage().getId());
                });

        Optional.ofNullable(storyImages)
                .filter(it -> !it.isEmpty())
                .orElse(Collections.emptyList())
                .forEach(it -> {
                    final EventStoryImageAddedEvent response = storeImageAndExecutePolicy(it,
                            (storedImage) -> addStoryImageToEventPolicy.tryExecute(UserAddedEventStoryImageEvent
                                    .create(newEventCreatedEvent.getEvent().getId(), storedImage)));
                    mediaBuilder.addStoryImage(response.getStoryImage().getId());
                });

        final Event event = newEventCreatedEvent.getEvent();
        final MediaEventResponseDto.MediaDto mediaDto = mediaBuilder.build();

        return EventModel.create(
                event,
                UserModel.create(event.getOwner(), 0, null),
                mediaDto.getTitleImage(),
                Collections.emptySet(),
                EventModel.ParticipantInfo.empty()
        );
    }

    private <T extends DomainEvent> T storeImageAndExecutePolicy(MultipartFile image,
                                                                 EventImageSupplier<T> domainLogicRunner) {
        final ImageEntity storedImage = mediaService.store(image);
        try {
            return domainLogicRunner.get(storedImage);
        } catch (Exception e) {
//                mediaService.remove(storedImage.getId());
            throw e;
        }
    }

    /**
     * @param <E> - response event {@link com.mzone.main.core.DomainEvent}
     */
    public interface EventImageSupplier<E> {
        E get(ImageEntity storedImage);
    }

    private static class EventUtils {

        public static FindEventModel toFindEventRequest(BigDecimal latitude,
                                                        BigDecimal longitude) {
            return new FindEventModel(latitude, longitude);
        }

        public static List<EventDto> toGetEventsDto(Collection<EventEntity> events) {
            return events.stream()
                    .map(Event::createFromEntity)
                    .map(EventDto::from)
                    .toList();
        }
    }

    @Data
    public static class EventDto {
        private final UUID id;
        private final String name;
        private final Set<Integer> categories;
        private final Integer availability;
        private final String description;
        private final LocationValue location;
        private final AccountDto owner;

        public static EventDto from(Event event) {
            final UserEntity owner = event.getOwner();
            final AccountDto ownerDto = new AccountDto(
                    owner.getId(),
                    owner.getFirstName(),
                    owner.getLastName(),
                    //// TODO: 09/06/2022 impl it
                    0,
                    null
            );

            return new EventDto(
                    event.getId(),
                    event.getName(),
                    event.getCategories().stream().map(Enum::ordinal).collect(Collectors.toSet()),
                    event.getAvailability(),
                    event.getDescription(),
                    event.getLocation(),
                    ownerDto
            );
        }

        @Data
        private static class AccountDto {
            private final UUID id;
            private final String firstName;
            private final String lastName;
            private final int followerCount;
            @Nullable
            private final String avatar;
        }
    }

    @Data
    private static class CreateEventDto {

        @Size(min = 1)
        @NotNull
        private String name;
        @NotNull
        private String description;
        /**
         * {@link com.mzone.main.events.event.EventCategory}
         */
        @NotEmpty
        private Collection<@EventCategoryConstraint Integer> categories;
        @NotNull
        private Integer availability;
        @NotNull
        private LocationDto location;
        @NotNull
        private DateTime startTime;
        @NotNull
        private DateTime endTime;
    }

    @Data
    private static class LocationDto {
        @NotNull
        private BigDecimal lat;
        @NotNull
        private BigDecimal lng;
    }

    @Data
    @Builder(setterPrefix = "set")
    @AllArgsConstructor
    private static class MediaEventResponseDto {

        private EventDto event;
        private MediaDto media;
        private Collection<ImageErrorDto> mediaErrors;

        @Data
        @AllArgsConstructor
        private static class MediaDto {
            private UUID titleImage;
            private Collection<UUID> storyImages;

            private static Builder newBuilder() {
                return new Builder();
            }

            private static class Builder {
                private final Collection<UUID> storyImages;
                private UUID titleImage;

                public Builder() {
                    storyImages = new LinkedList<>();
                }

                private Builder setTitleImage(UUID imageName) {
                    titleImage = imageName;
                    return this;
                }

                private Builder addStoryImage(UUID imageRef) {
                    storyImages.add(imageRef);
                    return this;
                }

                public MediaDto build() {
                    return new MediaDto(titleImage, storyImages.stream().toList());
                }
            }
        }

        @Data
        private static class ImageErrorDto {
            private String originalName;
            private String uploadedAt;
            private String size;
            private String errorMessage;
        }
    }
}