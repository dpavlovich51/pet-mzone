package com.mzone.main.events;

import com.mzone.main.core.BaseEntity;
import com.mzone.main.media.entity.ImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "event_media")
public class MediaEventEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;
    @Nullable
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_image_id")
    private ImageEntity titleImage;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_2_story_images",
            inverseJoinColumns = @JoinColumn(name = "story_image_id"),
            joinColumns = @JoinColumn(name = "event_media_id"))
    private Collection<ImageEntity> storyImages;

    public MediaEventEntity(EventEntity event,
                            ImageEntity titleImage,
                            Collection<ImageEntity> storyImages) {
        super(event.getId());
        this.event = event;
        this.titleImage = titleImage;
        this.storyImages = storyImages;
    }

    public static MediaEventEntity create(EventEntity event,
                                          ImageEntity titleImage) {
        return new MediaEventEntity(event,
                titleImage,
                Collections.emptyList());
    }

    public static MediaEventEntity create(UUID eventId) {
        final EventEntity event = new EventEntity();
        event.setId(eventId);

        return new MediaEventEntity(event,
                null,
                new ArrayList<>()
        );
    }

    public Optional<ImageEntity> getTitleImage() {
        return Optional.ofNullable(titleImage);
    }
}
