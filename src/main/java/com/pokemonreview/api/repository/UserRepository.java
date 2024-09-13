package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    //Este método es una consulta derivada. Spring Data JPA generará
    // automáticamente una consulta que buscará un UserEntity cuyo campo username coincida con el valor del argumento
    // username pasado al método. El uso de Optional<UserEntity> indica que el resultado de esta consulta puede ser
    //  un UserEntity, pero si no se encuentra ningún usuario con ese nombre de usuario, devolverá un Optional vacío
    //  en lugar de null. Esto ayuda a evitar errores de null y permite manejar casos donde no se encuentran resultados.
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

}
