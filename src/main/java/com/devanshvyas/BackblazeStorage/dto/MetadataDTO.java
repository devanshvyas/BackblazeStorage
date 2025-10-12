package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.metadata.BaseMetadata;
import com.devanshvyas.BackblazeStorage.model.metadata.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetadataDTO {
    private Integer id;
    private String name;
    private ContentType contentType;
    private String b2fileKey;
    private Long size;
    private Integer downloadCount;
    private Instant uploadedAt;
    private UserDto owner;

    public MetadataDTO(BaseMetadata baseMetadata) {
        this.id = baseMetadata.getId();
        this.name = baseMetadata.getName();
        this.contentType = baseMetadata.getContentType();
        this.b2fileKey = baseMetadata.getB2fileKey();
        this.size = baseMetadata.getSize();
        this.downloadCount = baseMetadata.getDownloadCount();
        this.uploadedAt = baseMetadata.getUploadedAt();
        this.owner = new UserDto(baseMetadata.getOwner(), null);
    }
}
