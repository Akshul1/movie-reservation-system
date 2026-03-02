package com.project.movie_reservation_system.service;

import com.project.movie_reservation_system.entity.Movie;
import com.project.movie_reservation_system.exception.MovieNotFoundException;
import com.project.movie_reservation_system.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tells JUnit to use Mockito features
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository; // Creates a "fake" repository

    @InjectMocks
    private MovieService movieService; // Injects the "fake" repo into the real service

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        // This runs before every test to provide fresh data
        sampleMovie = Movie.builder()
                .id(1L)
                .movieName("Inception")
                .movieLanguage("English")
                .build();
    }

    @Test
    void getMovieById_Success() {
        // 1. ARRANGE (Setup the fake behavior)
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));

        // 2. ACT (Call the actual service method)
        Movie result = movieService.getMovieById(1L);

        // 3. ASSERT (Check if the result is what we expected)
        assertNotNull(result);
        assertEquals("Inception", result.getMovieName());

        // Verify that findById was actually called exactly once
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getMovieById_NotFound_ThrowsException() {
        // 1. ARRANGE: Tell the mock to return "Empty"
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT: Verify that the correct exception is thrown
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.getMovieById(2L);
        });
    }
}