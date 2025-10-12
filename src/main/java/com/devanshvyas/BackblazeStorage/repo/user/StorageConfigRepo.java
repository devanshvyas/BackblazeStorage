package com.devanshvyas.BackblazeStorage.repo.user;

import com.devanshvyas.BackblazeStorage.model.user.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageConfigRepo extends JpaRepository<StorageConfig, Integer> {
    public StorageConfig findByUser(User user);
}
