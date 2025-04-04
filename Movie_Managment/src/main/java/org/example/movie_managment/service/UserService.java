package org.example.movie_managment.service;
import org.example.movie_managment.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO findUserByUsername(String username);
}

