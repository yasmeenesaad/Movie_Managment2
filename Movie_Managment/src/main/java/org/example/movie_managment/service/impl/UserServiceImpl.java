package org.example.movie_managment.service.impl;

import org.example.movie_managment.dto.UserDTO;
import org.example.movie_managment.mapper.UserMapper;
import org.example.movie_managment.model.Role;
import org.example.movie_managment.model.User;
import org.example.movie_managment.repository.UserRepository;
import org.example.movie_managment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);

        // Ensure password encoding
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPassword(userDTO.getPassword());
        user.setRole(Role.USER); // Default role is USER

        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDTO(savedUser);
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.INSTANCE.toDTO(user);
    }
}

