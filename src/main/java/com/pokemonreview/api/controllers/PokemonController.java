package com.pokemonreview.api.controllers;


import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonRespomse;
import com.pokemonreview.api.models.Pokemon;

import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


// Anotación que indica que esta clase es un controlador REST de Spring.
@RestController

// Especifica la ruta base para todas las rutas dentro de este controlador.
// En este caso, todas las rutas comenzarán con "/api/".
@RequestMapping("/api/")
public class PokemonController {

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    // Define una ruta GET para "/api/pokemon".
    // Este método será invocado cuando se realice una solicitud GET a esta ruta.
    @GetMapping("pokemon")
    public ResponseEntity<PokemonRespomse> getPokemons(
        @RequestParam(value="pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        return new ResponseEntity<>(pokemonService.getAllPokemons(pageNo,pageSize), HttpStatus.OK );
    }


    @GetMapping("/pokemon/{id}")
    public ResponseEntity<PokemonDto> pokemonDetail(@PathVariable int id) {
        return new ResponseEntity<>(pokemonService.getPokemonById(id), OK);

    }


    //Específicamente, asocia este método con solicitudes que usan el método POST.
    @PostMapping("pokemon/create")

    //Esta anotación le dice a Spring que devuelva el código de estado HTTP 201
    // (CREATED) cuando este método se ejecute con éxito.
    @ResponseStatus(HttpStatus.CREATED)
    //Este es el método que se ejecutará cuando se reciba una solicitud POST en la
    // ruta /pokemon/create.
    //@RequestBody indica que el cuerpo de la solicitud HTTP se deserializará
    // en un objeto Pokemon. Es decir, Spring tomará el JSON enviado en la solicitud
    // y lo convertirá en una instancia de la clase Pokemon.
    public ResponseEntity<PokemonDto> createPokemon(@RequestBody PokemonDto pokemonDto) {

        //Se está creando un nuevo ResponseEntity que contiene el objeto PokemonDto
        // que se recibió, y se está configurando el código de estado HTTP a 201 CREATED.
        return new ResponseEntity<>(pokemonService.createPokemon(pokemonDto), HttpStatus.CREATED );
    }

    @PutMapping("pokemon/update/{id}")
    public ResponseEntity<PokemonDto> updatePokemon(@PathVariable int id, @RequestBody PokemonDto pokemonDto) {

      return new ResponseEntity<>(pokemonService.updatePokemon(pokemonDto, id), HttpStatus.OK);
    }

    @DeleteMapping("pokemon/delete/{id}")
    public ResponseEntity<String> deletePokemon(@PathVariable int id) {
        pokemonService.deletePokemon(id);
      return new ResponseEntity<>("Pokemon delete", HttpStatus.OK);
    }

}
