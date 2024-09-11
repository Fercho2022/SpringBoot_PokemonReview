package com.pokemonreview.api.controllers;


import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Anotación que indica que esta clase es un controlador REST de Spring.
@RestController

// Especifica la ruta base para todas las rutas dentro de este controlador.
// En este caso, todas las rutas comenzarán con "/api/".
@RequestMapping("/api/pokemon")
public class ReviewController {

    private ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{pokemonId}/review")
    public ResponseEntity<ReviewDto> createReview(@PathVariable int pokemonId, @RequestBody ReviewDto reviewDto) {
        ReviewDto newReviewDto = reviewService.createReview(pokemonId, reviewDto);
        return new ResponseEntity<>(newReviewDto, HttpStatus.CREATED);
    }

    @GetMapping("/{pokemonId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByPokemonId(@PathVariable int pokemonId) {
        List<ReviewDto> reviewDtos = reviewService.getReviewsByPokemonId(pokemonId);
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    @GetMapping("/{pokemonId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable int pokemonId, @PathVariable int reviewId) {

        ReviewDto reviewDto = reviewService.getReviewById(reviewId, pokemonId);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @PutMapping("/{pokemonId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable int reviewId, @PathVariable int pokemonId, @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReviewDto = reviewService.updateReview(reviewId, pokemonId, reviewDto);
        return new ResponseEntity<>(updatedReviewDto, HttpStatus.OK);

    }

    @DeleteMapping("/{pokemonId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId, @PathVariable int pokemonId) {
        reviewService.deleteReview(reviewId, pokemonId);
        return new ResponseEntity<>("Review deleted", HttpStatus.OK);
    }
}


