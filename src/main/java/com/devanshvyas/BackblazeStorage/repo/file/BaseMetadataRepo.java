package com.devanshvyas.BackblazeStorage.repo.file;

import com.devanshvyas.BackblazeStorage.model.metadata.BaseMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseMetadataRepo<T extends BaseMetadata, ID extends Serializable> extends JpaRepository<T, ID> {

    void deleteByB2fileKey(String b2fileKey);

    void deleteByB2fileKeyIn(List<String> b2fileKeys);

    T findByB2fileKey(String fileName);

}
