package com.pokemonreview.api.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomUserDetailsService userDetailsService;

    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

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

                //Aquí defines un AuthenticationEntryPoint personalizado (jwtAuthEntryPoint) para manejar los
                // errores de autenticación, como los casos donde se intenta acceder sin autenticarse o con un JWT
                // inválido. El método commence de tu clase JwtAuthEntryPoint será invocado cuando ocurra un error
                //  de autenticación.). El propósito de este método es enviar una respuesta personalizada al cliente
                //   (generalmente una respuesta con código de estado 401 - No autorizado). Así es como tu aplicación
                //   maneja y responde a solicitudes no autenticadas o inválidas.
                //Internamente, cuando una excepción de autenticación es lanzada, Spring Security tiene
                // un manejador de excepciones que revisa qué AuthenticationEntryPoint está configurado.
                // En este caso, como has configurado tu clase JwtAuthEntryPoint, Spring Security delega
                // el manejo de esa excepción al commence de tu clase, el cual proporciona la respuesta
                // adecuada al cliente.
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthEntryPoint)
                )
                //.and(): Permite continuar con la siguiente configuración en la cadena de seguridad.


                //.sessionManagement(): Configura la política de manejo de sesiones. En este caso,
                // se está utilizando STATELESS, lo que significa que no se mantendrá ninguna sesión en el servidor.
                // Esto es útil para aplicaciones que funcionan con autenticación basada en JWT.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )



                //Permites el acceso a rutas relacionadas con la autenticación (/api/auth/**) sin necesidad
                // de autenticarse. Esto es típico para rutas de inicio de sesión o registro. Para cualquier
                // otra solicitud, se requiere autenticación.Permites el acceso a rutas relacionadas con la
                // autenticación (/api/auth/**) sin necesidad de autenticarse. Esto es típico para rutas de
                // inicio de sesión o registro. Para cualquier otra solicitud, se requiere autenticación.
                .authorizeRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()    // Permite acceso público a todas las solicitudes GET
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception {

        return authenticationConfiguration.getAuthenticationManager();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
