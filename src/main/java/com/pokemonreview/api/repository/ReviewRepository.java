package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
//Spring Data JPA genera automáticamente el código para el método findByPokemonId(int pokemonId)
// porque sigue la convención de nombres de métodos de consulta basados en propiedades (findBy).
// No tienes que escribir manualmente una clase que implemente este método.
    //El método findByPokemonId es un "query method", lo que significa que Spring Data
// JPA deduce la consulta SQL que debe ejecutarse basándose en el nombre del método.
//  En este caso, busca todas las reseñas (Review) que tengan un pokemonId específico.

    List<Review> findByPokemonId(int pokemonId);
    Review findByIdAndPokemonId(int reviewId, int pokemonId);
    boolean existsByIdAndPokemonId(int reviewId, int pokemonId);


}
