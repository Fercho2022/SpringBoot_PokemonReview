package com.pokemonreview.api.service.impl;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonRespomse;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.PokemonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PokemonRespomse getAllPokemons(int pageNo, int pageSize) {
//Se crea un objeto Pageable, que es una representación de una página solicitada con el número de
// página (pageNo) y el tamaño de la página (pageSize), usando la clase PageRequest. Esto permite
// que los resultados se dividan en páginas para manejar grandes cantidades de datos.
        //PageRequest: es una implementación de Pageable, y of() se utiliza para
        // crear una instancia con el número de página (pageNo) y el tamaño de la página (pageSize).
        Pageable pageable = (Pageable) PageRequest.of(pageNo, pageSize);

        //Se hace una consulta al repositorio de Pokémon (pokemonRepository) usando el
        // método findAll(pageable), lo que devuelve un objeto Page con los resultados paginados,
        //  en este caso, los objetos Pokemon.
        Page<Pokemon> pokemons=pokemonRepository.findAll((org.springframework.data.domain.Pageable) pageable);
//Obtiene el contenido real de la página, que es una lista de objetos Pokemon. El objeto Page
// contiene metadatos sobre la paginación (número de página, total de páginas, etc.),
// getContent(): devuelve la lista de elementos.
        List<Pokemon> listofPokemons=pokemons.getContent();
        //.stream(): Convierte la lista de Pokémon en un flujo de datos (stream), que permite operar
        // sobre los elementos de forma más funcional (es decir, aplicar transformaciones, filtros, etc.)
        //.map(pokemon -> mapToDto(pokemon)): Usa el método map() del stream para transformar
        // cada objeto Pokemon en un PokemonDto aplicando el método mapToDto(pokemon). El map()
        //  toma cada elemento del flujo y aplica la función proporcionada (en este caso, convertir
        //  un Pokemon a PokemonDto).
        //.collect(Collectors.toList()): Convierte el flujo transformado de vuelta a una lista.
        // Collectors.toList() recoge todos los elementos del flujo y los organiza en una lista.
        List<PokemonDto> content= listofPokemons.stream().map(pokemon->mapToDto(pokemon)).collect(Collectors.toList());
        PokemonRespomse pokemonResponse=new PokemonRespomse();
        pokemonResponse.setContent(content);
        pokemonResponse.setPageNo(pokemons.getNumber());
        pokemonResponse.setPageSize(pokemons.getSize());
        pokemonResponse.setTotalElements(pokemons.getTotalElements());
        pokemonResponse.setTotalPages(pokemons.getTotalPages());
        pokemonResponse.setLast(pokemons.isLast());
        return pokemonResponse;
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

    @Override
    public void deletePokemon(int id) {
        Pokemon pokemon= pokemonRepository.findById(id).orElseThrow(()->new PokemonNotFoundException("Pokemon could not be delete"));
    pokemonRepository.delete(pokemon);

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
