package org.example.movie_managment.dto;
import lombok.*;
import org.example.movie_managment.model.Role;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Role role;

    public String getPassword() {
        return password;
    }
}

