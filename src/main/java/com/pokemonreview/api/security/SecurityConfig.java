package com.pokemonreview.api.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
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
                .authorizeRequests(auth -> auth.anyRequest().authenticated())

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
}
