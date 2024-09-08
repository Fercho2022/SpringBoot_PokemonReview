package com.pokemonreview.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals, y hashCode automáticamente
@AllArgsConstructor // Genera un constructor con todos los campos como parámetros
@NoArgsConstructor  // Genera un constructor sin argumentos
@Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla en la base de datos
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private int stars;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pokemon_id")
    private Pokemon pokemon;
}
