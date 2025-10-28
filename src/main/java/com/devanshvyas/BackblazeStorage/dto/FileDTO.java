package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.metadata.BaseMetadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {
    private List<String> filenames;
    private List<MetadataDTO> files;
    private int size;
    private int page;
    private int totalPage;
    private boolean isLast;

    public FileDTO(List<BaseMetadata> metadata) {
        this.files = metadata.stream()
                .map(MetadataDTO::new)
                .toList();
    }

    public FileDTO(Page<BaseMetadata> metadata) {
        this.files = metadata.getContent()
                .stream()
                .map(MetadataDTO::new)
                .toList();
        this.totalPage = metadata.getTotalPages();
        this.isLast = metadata.isLast();
        this.size = metadata.getSize();
        this.page = metadata.getNumber();
    }
}
