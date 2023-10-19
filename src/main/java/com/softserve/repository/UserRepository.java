package com.softserve.repository;

import com.softserve.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BasicRepository<User, Long> {

    /**
     * Retrieves a user by email from the database.
     *
     * @param email the string represents user's email
     * @return an Optional describing the user or an empty Optional if none found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user by token from the database.
     *
     * @param token the string represents the user's token
     * @return an Optional describing the user or an empty Optional if none found
     */
    Optional<User> findByToken(String token);

    /**
     * Returns all users from the database, that have role USER in system.
     *
     * @return the list of users that have role USER
     */
    List<User> getAllUsersWithRoleUser();
}
