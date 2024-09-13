package com.pokemonreview.api.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

// La anotación @Bean se utiliza para indicar que el método FilterChain produce un bean que debe ser gestionado
//  por el contenedor de Spring. Esto permite que Spring cree y gestione la instancia de SecurityFilterChain
//  y la inyecte donde sea necesario.

    @Bean
    //Configuración de Seguridad: El método FilterChain configura la seguridad de la aplicación. Spring
    // Security utiliza la instancia de SecurityFilterChain para aplicar las reglas de seguridad a las
    // solicitudes HTTP que llegan a la aplicación. Estas configuraciones incluyen el manejo de CSRF,
    // la autorización de solicitudes, y el tipo de autenticación a utilizar.
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        //.csrf(csrf -> csrf.disable()): Deshabilita la protección contra ataques CSRF. Esto es común en aplicaciones
        // API donde no es necesario manejar formularios o sesiones. Sin embargo, para aplicaciones web donde los usuarios
        // inician sesión, puede ser recomendable no deshabilitar CSRF.
        http

                //.csrf(csrf -> csrf.disable()): Deshabilita la protección contra ataques CSRF. Esto es común en aplicaciones
                // API donde no es necesario manejar formularios o sesiones. Sin embargo, para aplicaciones web donde los usuarios
                // inician sesión, puede ser recomendable no deshabilitar CSRF.
                .csrf(csrf -> csrf.disable())

                //.authorizeRequests(auth -> auth.anyRequest().authenticated()): Asegura que cualquier solicitud que se
                // realice a la aplicación esté autenticada. Todos los endpoints estarán protegidos por autenticación.
                .authorizeRequests(auth -> auth
                .requestMatchers(HttpMethod.GET).permitAll()    // Permite acceso público a todas las solicitudes GET
                        .anyRequest().authenticated())          // Requiere autenticación para cualquier otra solicitud
                //.httpBasic(withDefaults()): Habilita la autenticación HTTP Basic, que solicita a los usuarios
                // un nombre de usuario y una contraseña a través del navegador o cliente de API.
                .httpBasic(withDefaults()); // Enable HTTP Basic authentication

        //http.build(): Este método se encarga de ensamblar todas las configuraciones que has definido usando la instancia de HttpSecurity.
        // Internamente, configura un conjunto de filtros de seguridad que Spring Security usa para inspeccionar y manejar cada solicitud HTTP.
        //Al ejecutar http.build(), estás diciendo "OK, he terminado de configurar las reglas de seguridad, ahora construye la cadena de filtros
        // y úsala en mi aplicación". En resumen, return http.build(); devuelve la cadena de filtros de seguridad (un SecurityFilterChain),
        // que luego Spring Boot utiliza para proteger tu aplicación según las reglas que has definido.
        return http.build();

    }
    @Bean
    public UserDetailsService user() {
        //Cada método devuelve el mismo objeto sobre el que se está trabajando (en este caso, un objeto
        // User.Builder). Esto permite que el siguiente método se aplique directamente al mismo objeto sin
        //  necesidad de crear variables intermedias.
        //Cuando llamas a un método, como User.builder(), este devuelve un objeto de tipo User.Builder.
        // Luego puedes llamar a otros métodos, como .username(), que también devolverá el mismo objeto
        // User.Builder. Así, los métodos se pueden "encadenar" uno tras otro, porque cada método devuelve
        //  el mismo objeto que el siguiente método utilizará.

    UserDetails admin= User.builder().username("admin").password("password").roles("ADMIN").build();
    UserDetails user= User.builder().username("user").password("password").roles("USER").build();

    //Aquí se retorna una instancia de InMemoryUserDetailsManager, que es una implementación de
        // UserDetailsService que almacena los detalles de los usuarios en memoria (en lugar de en una base
        // de datos u otro sistema de almacenamiento persistente). Esta clase gestiona los usuarios creados
        // (admin y user), permitiendo que Spring Security los utilice para la autenticación. Los usuarios
        // y roles definidos estarán disponibles durante la ejecución de la aplicación, pero no se
        // persistirán después de que la aplicación se cierre.
    return new InMemoryUserDetailsManager(admin,user);

}

}
