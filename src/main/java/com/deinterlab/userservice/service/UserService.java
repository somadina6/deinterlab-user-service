package com.deinterlab.userservice.service;

import com.deinterlab.userservice.dto.UserDTO;
import com.deinterlab.userservice.exception.UserException;
import com.deinterlab.userservice.model.AuthResponse;
import com.deinterlab.userservice.model.User;
import com.deinterlab.userservice.repository.UserRepository;
import com.deinterlab.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Create a new user
     *
     * @param user user details
     * @return response
     */
    public AuthResponse createUser(User user) {
        // Check if the user already exists
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserException("User already exists", HttpStatus.BAD_REQUEST);
        }
        // Create a new User object
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        User newUser = new User(user.getEmail(), hashedPassword, user.getFirstName(), user.getLastName(), user.getPhoneNumber());

        // Save the user to the database
        userRepository.save(newUser);

        return new AuthResponse(200, "User created successfully");
    }

    /**
     * Authenticate user and return a JWT token
     *
     * @param email    user email
     * @param password user password
     * @return token
     */
    public AuthResponse authenticateUser(String email, String password) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);

        if (userByEmail.isEmpty()) {
            throw new UserException("User not found", HttpStatus.BAD_GATEWAY);
        } else if (BCrypt.checkpw(password, userByEmail.get().getPassword())) {
            UserDTO userDTO = new UserDTO(
                    userByEmail.get().getId(),
                    userByEmail.get().getEmail(),
                    userByEmail.get().getFirstName(),
                    userByEmail.get().getLastName(),
                    userByEmail.get().getRole()
            );
            String token = jwtUtil.generateToken(userDTO);
            return new AuthResponse(token);
        } else {
            throw new UserException("Invalid email / password", HttpStatus.UNAUTHORIZED);
        }

    }

    private User checkUserExistsAndReturnUser(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public String getUserRole(String email) {
        User user = checkUserExistsAndReturnUser(email);
        return user.getRole();
    }

    public void updateUserRole(String email, String role) {
        User user = checkUserExistsAndReturnUser(email);
        user.setRole(role);
        userRepository.save(user);
    }

    public void deleteUser(String email) {
        User user = checkUserExistsAndReturnUser(email);
        userRepository.delete(user);
    }

    public void updateUserPassword(String email, String password) {
        User user = checkUserExistsAndReturnUser(email);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public void updateUserEmail(String email, String newEmail) {
        User user = checkUserExistsAndReturnUser(email);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public Iterable<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return checkUserExistsAndReturnUser(email);
    }

    public User getUserById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
