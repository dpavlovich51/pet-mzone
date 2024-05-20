package com.mzone.main.core;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    private UUID id;
    @Column(name = "created_at")
    private DateTime createdAt;
    @Column(name = "updated_at")
    private DateTime updatedAt;
    @Column(name = "deleted_at")
    private DateTime deletedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public BaseEntity() {
    }

    public BaseEntity(UUID id) {
        this.id = id;

        //// TODO: 06/07/2022 impl logic
        createdAt = UTime.timeNow();
        updatedAt = UTime.timeNow();
        deletedAt = UTime.timeNow();
        isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
