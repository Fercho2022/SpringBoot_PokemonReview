package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonRespomse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PokemonService {

    PokemonDto createPokemon(PokemonDto pokemonDto);
    PokemonRespomse getAllPokemons(int pageNo, int pageSize);
    PokemonDto getPokemonById(int id);
    PokemonDto updatePokemon(PokemonDto pokemonDto, int id);
    void deletePokemon(int id);

}
