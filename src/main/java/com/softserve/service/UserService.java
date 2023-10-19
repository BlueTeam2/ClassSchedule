package com.softserve.service;

import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Optional;

public interface UserService extends BasicService<User, Long> {

    /**
     * Retrieves a user by email from the repository.
     *
     * @param email the string represents user's email
     * @return the found user
     * @throws com.softserve.exception.EntityNotFoundException if user with given email is not found
     */
    User findByEmail(String email);

    /**
     * Retrieves a user by token from the repository.
     *
     * @param token the string represents the user's token
     * @return the founded user
     * @throws com.softserve.exception.EntityNotFoundException if user with given token is not found
     */
    User findByToken(String token);

    /**
     * Registers the given user in the system.
     *
     * @param user the user used for registration in the system
     * @return User entity
     * @throws com.softserve.exception.IncorrectPasswordException when password is incorrect or not strong enough
     */
    User registration(User user);

    /**
     * Resets the user password.
     *
     * @param email the string represents user's email
     */
    void resetPassword(String email);

    /**
     * Creates user after sign in with Oauth2 Social.
     *
     * @param oAuth2User the user credentials to save
     * @return created user
     */
    User createSocialUser(OAuth2User oAuth2User);

    /**
     * Returns user by email from the repository.
     *
     * @param email the string represents user's email
     * @return an Optional describing the user or an empty Optional if none found
     */
    Optional<User> findSocialUser(String email);

    /**
     * Returns all users from the repository, that have role USER.
     *
     * @return the list of users that have role USER
     */
    List<User> getAllUsersWithRoleUser();

    /**
     * Returns the new encoded password for the given user.
     *
     * @param user        the user
     * @param oldPassword the string represents previous raw password
     * @param newPassword the string represents new raw password
     * @return the string represents new encoded password
     * @throws com.softserve.exception.IncorrectPasswordException if previous password was incorrect or new one not strong enough
     */
    String changePasswordForCurrentUser(User user, String oldPassword, String newPassword);

    /**
     * Performs automatic user registration in the system.
     *
     * @param email the string represents email for user registration
     * @param role  the role of the new user
     * @return the new registered user
     * @throws com.softserve.exception.IncorrectPasswordException if password was incorrect or not strong enough
     */
    User automaticRegistration(String email, Role role);

}

