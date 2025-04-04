package org.example.movie_managment.exception;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OmdbApiException.class)
    public String handleOmdbApiException(OmdbApiException e, Model model) {
        model.addAttribute("errorMessage", "OMDB API Error: " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return "error";
    }
}