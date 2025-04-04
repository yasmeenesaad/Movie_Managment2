package org.example.movie_managment.dto;
import lombok.*;


@Data
public class UserDto {
    private String username;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    private String password;
    private boolean admin;
}

