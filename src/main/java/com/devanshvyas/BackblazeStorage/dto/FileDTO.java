package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.metadata.BaseMetadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {
    private List<String> filenames;
    private List<MetadataDTO> files;

    public FileDTO(List<BaseMetadata> metadata) {
        this.files = metadata.stream()
                .map(MetadataDTO::new)
                .toList();
    }
}
