package com.devanshvyas.BackblazeStorage.repo;

import com.devanshvyas.BackblazeStorage.model.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageConfigRepo extends JpaRepository<StorageConfig, Integer> {
    public StorageConfig findByUser(User user);
}
