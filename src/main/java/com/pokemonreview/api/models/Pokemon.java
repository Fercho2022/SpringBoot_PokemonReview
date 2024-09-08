package com.pokemonreview.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data // Genera getters, setters, toString, equals, y hashCode automáticamente
@AllArgsConstructor // Genera un constructor con todos los campos como parámetros
@NoArgsConstructor  // Genera un constructor sin argumentos
@Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla en la base de datos
public class Pokemon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<Review>();

}
