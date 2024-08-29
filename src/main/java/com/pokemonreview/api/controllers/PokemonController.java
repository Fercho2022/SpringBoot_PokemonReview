package com.pokemonreview.api.controllers;


import com.pokemonreview.api.models.Pokemon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


// Anotación que indica que esta clase es un controlador REST de Spring.
@RestController

// Especifica la ruta base para todas las rutas dentro de este controlador.
// En este caso, todas las rutas comenzarán con "/api/".
@RequestMapping("/api/")
public class PokemonController {
    // Define una ruta GET para "/api/pokemon".
    // Este método será invocado cuando se realice una solicitud GET a esta ruta.
    @GetMapping("pokemon")
    public ResponseEntity<List<Pokemon>> getPokemons() {
        //// Crear una lista de pokémons. List es una interfaz en Java, mientras que ArrayList
        // es una implementación concreta de esa interfaz, esto nos permite
        List<Pokemon> pokemons = new ArrayList<>();
        pokemons.add(new Pokemon(1, "Squirtle", "Water"));
        pokemons.add(new Pokemon(2, "Pikachu", "Electric"));
        pokemons.add(new Pokemon(2, "Charmander", "Fire"));
        pokemons.add(new Pokemon(3, "Bulbasaur", "Grass/Poison"));

        // Retornar la lista de pokemons envuelta en un ResponseEntity
        return ResponseEntity.ok(pokemons);
    }



}
