package com.pokemonreview.api.service.impl;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.PokemonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonServiceImpl implements PokemonService {

    private PokemonRepository pokemonRepository;

    public PokemonServiceImpl(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public PokemonDto createPokemon(PokemonDto pokemonDto) {
        Pokemon pokemon=new Pokemon();
        pokemon.setName(pokemonDto.getName());
        pokemon.setType(pokemonDto.getType());

        Pokemon newPokemon=pokemonRepository.save(pokemon);
        PokemonDto pokemonResponse=new PokemonDto();
        pokemonResponse.setId(newPokemon.getId());
        pokemonResponse.setName(newPokemon.getName());
        pokemonResponse.setType(newPokemon.getType());
        return pokemonResponse;

    }

    @Override
    public List<PokemonDto> getAllPokemons() {


        List<Pokemon> pokemons= pokemonRepository.findAll();

        //stream(): Convierte la lista en un flujo de datos (stream), lo que permite operar sobre la
        // lista de forma más funcional.
        //map(): Aplica una transformación a cada elemento del stream. En este caso, transforma cada objeto
        // Pokemon en un PokemonDto mediante el método mapToDto().
        //collect(Collectors.toList()): Convierte el flujo modificado nuevamente en una lista.
        return pokemons.stream().map(pokemon -> mapToDto(pokemon)).collect(Collectors.toList());
    }

    @Override
    public PokemonDto getPokemonById(int id) {
        //el método findById que pertenece al repositorio de JPA en Spring Data, este método devuelve
        // un objeto de tipo Optional<T>, en este caso, Optional<Pokemon>. El propósito de Optional es evitar
        // el uso directo de valores null y facilitar el manejo de la posibilidad de que el valor no esté
        // presente (es decir, cuando no se encuentra el Pokemon con el id proporcionado).
        //orElseThrow(): es un método de la clase Optional que devuelve el valor contenido si está
        // presente o lanza una excepción personalizada PokemonNotFoundException si el valor no está.
        Pokemon pokemon=pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFoundException("Pokemon could not be found"));
        return mapToDto(pokemon);
    }

    @Override
    public PokemonDto updatePokemon(PokemonDto pokemonDto, int id) {
        Pokemon pokemon= pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFoundException("Pokemon could not be found"));
        pokemon.setName(pokemonDto.getName());
        pokemon.setType(pokemonDto.getType());
        Pokemon updatedPokemon=pokemonRepository.save(pokemon);
        return mapToDto(updatedPokemon);

    }

    private PokemonDto mapToDto(Pokemon pokemon) {
        PokemonDto pokemonDto=new PokemonDto();
        pokemonDto.setId(pokemon.getId());
        pokemonDto.setName(pokemon.getName());
        pokemonDto.setType(pokemon.getType());
        return pokemonDto;
    }

    private Pokemon mapToEntity(PokemonDto pokemonDto) {
        Pokemon pokemon=new Pokemon();
        pokemon.setId(pokemonDto.getId());
        pokemon.setName(pokemonDto.getName());
        pokemon.setType(pokemonDto.getType());
        return pokemon;

    }



}
