package com.pokemonreview.api.controllers;


import com.pokemonreview.api.models.Pokemon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


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
        pokemons.add(new Pokemon(3, "Charmander", "Fire"));
        pokemons.add(new Pokemon(4, "Bulbasaur", "Grass/Poison"));

        // Retornar la lista de pokemons envuelta en un ResponseEntity
        return ResponseEntity.ok(pokemons);
    }


    @GetMapping("/pokemon/{id}")
    public Pokemon pokemonDetail(@PathVariable int id) {
        return new Pokemon(id, "Squirtle", "Water");
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
    public ResponseEntity<Pokemon> createPokemon(@RequestBody Pokemon pokemon) {
        System.out.println(pokemon.getName());
        System.out.println(pokemon.getType());

        //Se está creando un nuevo ResponseEntity que contiene el objeto Pokemon
        // que se recibió, y se está configurando el código de estado HTTP a 201 CREATED.
        return new ResponseEntity<>(pokemon, HttpStatus.CREATED );
    }

    @PutMapping("pokemon/update/{id}")
    public ResponseEntity<Pokemon> updatePokemon(@PathVariable int id, @RequestBody Pokemon pokemon) {
        System.out.println(pokemon.getName());
        System.out.println(pokemon.getType());
        return ResponseEntity.ok(pokemon);

    }

    @DeleteMapping("pokemon/delete/{id}")
    public ResponseEntity<String> deletePokemon(@PathVariable int id) {

        System.out.println(id);
        return ResponseEntity.ok("Pokemon Deleted successfully ");
    }

}
