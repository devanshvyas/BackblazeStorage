package com.devanshvyas.BackblazeStorage.repo.user;

import com.devanshvyas.BackblazeStorage.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    public User findByUsername(String username);
}
