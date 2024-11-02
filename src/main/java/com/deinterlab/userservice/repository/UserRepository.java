package com.deinterlab.userservice.repository;

import com.deinterlab.userservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

        Optional<User> findUserByEmail(String email);
}
