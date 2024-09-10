package com.pokemonreview.api.service.impl;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.exceptions.ReviewNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private PokemonRepository pokemonRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, PokemonRepository pokemonRepository) {
        this.reviewRepository = reviewRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public ReviewDto createReview(int pokemonId, ReviewDto reviewDto) {
        // Buscar el Pokémon por ID
      Pokemon pokemon=pokemonRepository.findById(pokemonId).orElseThrow(()->new RuntimeException("Pokemon not found"));

      // Crear la nueva reseña
      Review review=new Review();
      review.setTitle(reviewDto.getTitle());
      review.setContent(reviewDto.getContent());
      review.setStars(reviewDto.getStars());
      review.setPokemon(pokemon);

        // Guardar la reseña
        review= reviewRepository.save(review);

        // Convertir la reseña guardada a ReviewDto y devolverla
   return mapToDto(review);



    }

    @Override
    public List<ReviewDto> getReviewsByPokemonId(int pokemonId){
        // Buscar el Pokémon por ID
        Pokemon pokemon= pokemonRepository.findById(pokemonId).orElseThrow(()->new RuntimeException("Pokemon not found"));

        // Obtener las reseñas asociadas a ese Pokémon
        List<Review> reviews= reviewRepository.findByPokemonId(pokemonId);

        // Convertir la lista de Review a ReviewDto
        return reviews.stream().map(this::mapToDto).collect(Collectors.toList());

    }

    @Override
    public ReviewDto getReviewById(int reviewId, int pokemonId) {

        // Verificar si la Review existe y pertenece al Pokémon

        boolean exists=reviewRepository.existsByIdAndPokemonId(reviewId, pokemonId);
        if(!exists){
            throw new ReviewNotFoundException("Review with the associated Pokemon not found");
        }
        // Obtener la Review después de verificar la relación

        Review review= reviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("Review not found"));

        // Convertir la Review a ReviewDto
        return mapToDto(review);
    }


    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto=new ReviewDto();
        reviewDto.setStars(review.getStars());
        reviewDto.setTitle(review.getTitle());
        reviewDto.setContent(review.getContent());
        return reviewDto;
    }

    private Review mapToEntity(ReviewDto reviewDto) {
        Review review=new Review();

        review.setStars(reviewDto.getStars());
        review.setContent(reviewDto.getContent());
        review.setTitle(reviewDto.getTitle());


        return review;

    }
}
