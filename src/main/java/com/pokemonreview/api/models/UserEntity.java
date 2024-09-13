package com.pokemonreview.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity     //Marca la clase como una entidad JPA, lo que significa que esta clase será mapeada a una tabla
// en la base de datos. Le dice a JPA que esta clase representará una tabla en la base de datos y que las instancias de
// esta clase se convertirán en filas de la tabla.

@Table(name="users")    //Especifica el nombre de la tabla a la que se mapeará la entidad. En este caso,
// la entidad UserEntity se mapeará a la tabla llamada users. Si no se usa esta anotación, JPA nombraría
// la tabla automáticamente con el nombre de la clase, pero aquí se define explícitamente.

@Data   //Esta anotación reduce el código repetitivo, ya que no es necesario escribir manualmente
// getters y setters. Todo esto es generado en tiempo de compilación.

@NoArgsConstructor  //Genera un constructor sin argumentos. Esta anotación también es de Lombok
// y permite la creación automática de un constructor vacío (sin parámetros) para la clase UserEntity.
public class UserEntity {
    @Id //JPA usa este campo para identificar de manera única cada registro en la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que el valor de la clave primaria será
    // generado automáticamente por la base de datos.
    private int id;
    private String username;
    private String password;
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL) //Cada usuario puede tener múltiples roles y
    // cada rol puede estar asignado a múltiples usuarios. Con FetchType.EAGER, los roles asociados al
    // usuario se cargan automáticamente junto con el usuario. El CascadeType.ALL asegura que cualquier
    // operación (guardar, borrar, etc.) que se realice en un UserEntity también se aplique a los roles
    // relacionados.
    @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id") ) //La anotación @JoinTable
    // define la tabla intermedia user_roles, que se utilizará para mapear la relación entre usuarios y roles.
    // joinColumns especifica la columna que corresponde al user_id y inverseJoinColumns define la columna
    // role_id para asociar los roles.
    private List<Role> roles= new ArrayList<>();    //Inicializa la lista de roles en un ArrayList vacío.
    // Cada UserEntity puede tener varios Role asignados, y la lista se usará para almacenar esos roles.


}
