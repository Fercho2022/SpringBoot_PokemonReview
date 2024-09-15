package com.pokemonreview.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pokemonreview.api.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Este método es una consulta derivada generada automáticamente por Spring Data JPA, basada en el nombre del
    // método. Busca un rol en la base de datos cuyo campo name coincida con el parámetro name proporcionado.
    //El método devuelve un Optional<Role>, lo que significa que puede o no encontrar un rol con ese nombre.
    // Si lo encuentra, lo devuelve encapsulado en un Optional; si no lo encuentra, devuelve un Optional.empty().
    Optional<Role> findByName(String name);
}
