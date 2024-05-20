package com.mzone.main.media.entity;

import com.mzone.main.core.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {

    @Column(name = "name_suffix")
    private String nameSuffix;
    @Column(name = "name_prefix")
    private String namePrefix;
    @Column(name = "original_name")
    private String originalName;
    @Lob
    @Column(name = "data")
    private byte[] data;
    @Column(name = "created_at")
    private DateTime createdAt;

    public FileEntity(UUID id,
                      String nameSuffix,
                      String namePrefix,
                      String originalName,
                      byte[] data,
                      DateTime createdAt) {
        super(id);
        this.nameSuffix = nameSuffix;
        this.namePrefix = namePrefix;
        this.originalName = originalName;
        this.data = data;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return getNamePrefix().concat(".").concat(getNameSuffix());
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "nameSuffix='" + nameSuffix + '\'' +
                ", namePrefix='" + namePrefix + '\'' +
                ", originalName='" + originalName + '\'' +
                ", createdAt=" + createdAt +
                "} " + super.toString();
    }
}
