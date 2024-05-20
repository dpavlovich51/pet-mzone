package com.mzone.main.media.entity;

import com.mzone.main.core.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class ImageEntity extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "reference")
    private String reference;
    @Column(name = "extension")
    private String extension;

    public ImageEntity(UUID id,
                       String name,
                       String reference,
                       String extension) {
        super(id);
        this.name = name;
        this.reference = reference;
        this.extension = extension;
    }

}
