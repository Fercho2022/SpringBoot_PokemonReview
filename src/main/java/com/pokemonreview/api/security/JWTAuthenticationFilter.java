package com.pokemonreview.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JWTAuthenticationFilter: Es una clase que extiende de OncePerRequestFilter, lo que significa que será ejecutada
// una vez por cada solicitud HTTP entrante. El propósito de este filtro es interceptar las solicitudes y verificar
// si contienen un token JWT válido, para luego establecer la autenticación en el contexto de seguridad.
//OncePerRequestFilter: Clase abstracta base de Spring Security para filtros que se ejecutan solo una vez por
// solicitud.
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    //JWTGenerator tokenGenerator: Clase encargada de generar y validar los tokens JWT.
@Autowired
    private  JWTGenerator tokenGenerator;
    //CustomUserDetailsService userDetailsService: Servicio que carga los detalles del
    // usuario (por ejemplo, desde una base de datos) basándose en el nombre de usuario obtenido del token.
@Autowired
    private  CustomUserDetailsService userDetailsService;

    //Inyecta el generador de tokens y el servicio de detalles del usuario en la clase.



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //String token = getJWTFromRequest(request);: Obtiene el token JWT de la cabecera de
        // autorización de la solicitud HTTP. Este token será procesado más adelante.
            String token= getJWTFromRequest(request);

            // Verifica si el token no está vacío y si es un token válido utilizando el método
            // validateToken del JWTGenerator.
            if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {

                // Extrae el nombre de usuario codificado dentro del token JWT.
                String username= tokenGenerator.getUsernameFromJwt(token);

            //Carga los detalles del usuario basándose en el nombre de usuario extraído del token.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //Crea un token de autenticación de Spring Security con los detalles del usuario (roles, permisos, etc.).
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //Establece detalles adicionales de la solicitud, como la IP y la sesión, en el token de autenticación.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //Almacena la autenticación en el contexto de seguridad de Spring, lo que permite que la solicitud sea tratada como
            // autenticada en las siguientes operaciones.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            // Continúa con la ejecución de la cadena de filtros, permitiendo que otros filtros también procesen la
        // solicitud.
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        // Obtener el token JWT de la cabecera de autorización
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
/* Voy a explicarte en detalle, paso a paso, cómo funciona el filtro JWTAuthenticationFilter y lo que ocurre
durante su ejecución, pero sin usar código.

Interceptar la solicitud:
Cada vez que un cliente (por ejemplo, una aplicación web o móvil) realiza una solicitud HTTP a tu API, este filtro
intercepta dicha solicitud antes de que llegue al controlador. El objetivo principal de este filtro es verificar si la solicitud contiene un token JWT (JSON Web Token) válido
en la cabecera de autorización.

Obtener el token JWT:
El filtro primero revisa la cabecera de la solicitud HTTP, donde se suele incluir el token
JWT. La cabecera de autorización debería comenzar con la palabra "Bearer", seguida del token JWT.
Si la cabecera está presente y empieza con "Bearer", el filtro extrae el token (es decir, corta la parte que dice
"Bearer " y toma el resto, que es el token en sí). Si no hay ningún token presente o no comienza correctamente con "Bearer", el filtro sigue sin hacer nada especial y simplemente permite que la solicitud continúe sin autenticación.

Validar el token JWT:
Si se encontró un token, el filtro lo pasa a un servicio que se encarga de validarlo. Este servicio verifica que
el token sea correcto, que no haya expirado y que no haya sido manipulado. Si el token no es válido (por ejemplo,
porque fue alterado o ya expiró), la solicitud se considera no autenticada y el filtro deja que continúe sin
modificar el estado de autenticación.

Obtener el usuario del token:
Si el token es válido, el filtro extrae la información del nombre de usuario (u otra identificación) que está
guardada dentro del token. Un JWT normalmente contiene detalles como el nombre de usuario del cliente autenticado.

Cargar los detalles del usuario:
Una vez que el nombre de usuario es extraído del token, el filtro llama a un servicio que se encarga de cargar los detalles completos del usuario desde una base de datos o cualquier otra fuente de datos. Esto incluiría información como las contraseñas (aunque no las necesitamos para este caso), roles o permisos del usuario.
Autenticación en el sistema: Ahora que tenemos los detalles del usuario y sabemos que el token es válido, se crea un objeto de autenticación en el sistema de seguridad de Spring (Spring Security). Este objeto representa a un usuario autenticado, con toda la información sobre sus roles y permisos.
El filtro también puede añadir detalles adicionales a la autenticación, como la dirección IP desde la que se hizo la solicitud o detalles de la sesión del usuario.

Registrar la autenticación:
El filtro almacena esta información en un contexto de seguridad global, para que cualquier otra parte de la
aplicación (otros filtros, controladores, servicios) pueda saber que esta solicitud ha sido autenticada y
puede actuar en consecuencia. Esto permite a la aplicación tratar al usuario como autenticado y otorgarle acceso
a recursos protegidos, como acciones específicas o información sensible.

Continuar la solicitud:
Finalmente, el filtro permite que la solicitud continúe su curso. Si se ha autenticado correctamente, el sistema
ya tratará la solicitud como si fuera de un usuario autenticado. Si no, la solicitud se procesará como la de un
usuario anónimo o no autenticado. Si la autenticación falla o el token no es válido, la solicitud no será
rechazada inmediatamente en este filtro, pero la aplicación podría rechazarla más adelante cuando se intente
acceder a un recurso protegido.

Resumen final:
En resumen, este filtro intercepta cada solicitud, busca un token JWT en la cabecera, lo valida, extrae el
usuario del token, autentica al usuario en el sistema de seguridad, y luego permite que la solicitud siga su
curso. Si el token es válido, el usuario es tratado como autenticado. Si no, se procesa la solicitud sin
autenticación o con acceso limitado.
 */