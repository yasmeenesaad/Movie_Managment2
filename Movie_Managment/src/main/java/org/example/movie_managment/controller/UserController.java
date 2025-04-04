package org.example.movie_managment.controller;
import org.example.movie_managment.dto.UserDTO;
import org.example.movie_managment.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }
}
