package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import org.springframework.stereotype.Service;


public interface PokemonService {

    PokemonDto createPokemon(PokemonDto pokemonDto);
}
