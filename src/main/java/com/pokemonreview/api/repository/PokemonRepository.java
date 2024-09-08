package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
    Page<Pokemon> findAll(Pageable pageable);
}
