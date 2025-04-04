package org.example.movie_managment.service.impl;
import org.example.movie_managment.dto.UserDto;
import org.example.movie_managment.service.UserService;
import org.springframework.stereotype.Service;
import org.example.movie_managment.model.User;
import org.example.movie_managment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(UserDto userDto) {
        // Validate input
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAdmin(userDto.isAdmin());

        // Save user
        userRepository.save(user);
    }
}



