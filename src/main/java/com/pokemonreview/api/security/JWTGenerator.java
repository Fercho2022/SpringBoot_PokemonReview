package com.pokemonreview.api.security;


import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SignatureException;
import java.util.Date;

//@Component: Le indica a Spring que esta clase debe tratarse como un "bean", lo que permite que sea
// automáticamente detectada y registrada en el contexto de la aplicación. Al marcar esta clase como un
// componente, puede ser utilizada en otras clases a través de la inyección de dependencias.
@Component          //
public class JWTGenerator {

//Este método genera un JWT basado en la información del usuario autenticado proporcionada a través del
// objeto Authentication. El token generado será utilizado para autenticar al usuario en futuras solicitudes.

    public String generateToken(Authentication authentication) {
        //authentication.getName(): Obtiene el nombre de usuario (generalmente el nombre o email) del usuario
        // autenticado. Este nombre de usuario se utiliza como el "sujeto" (subject) del JWT, que identifica al
        // usuario.
        String username = authentication.getName();
        //new Date(): Crea una instancia de la clase Date que representa el momento actual. Esta fecha se
        // utilizará como la fecha de emisión del token.
            Date currentDate = new Date();
         //currentDate.getTime(): Obtiene el tiempo actual en milisegundos desde el 1 de enero de 1970 (epoch time).
        //SecurityConstants.JWT_EXPIRATION: Esta es una constante que probablemente defines en otra clase llamada
        // SecurityConstants. Representa el tiempo de expiración del token en milisegundos. La fecha de expiración
        // se establece sumando este valor al tiempo actual.
            Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
//Jwts.builder(): Este es el constructor de tokens JWT. Permite configurar las diferentes partes del token.
        String token= Jwts.builder()
             //setSubject(username): Define el sujeto del token, que en este caso es el nombre de usuario autenticado.
                // El sujeto es uno de los principales atributos de un JWT, que se utiliza para identificar
                // al usuario que posee el token.
                .setSubject(username)
                //setIssuedAt(currentDate): Establece la fecha de emisión del token. Indica cuándo fue creado el JWT.
                .setIssuedAt(currentDate)
                //setExpiration(expirationDate): Establece la fecha de expiración del token. Define cuándo el JWT
                // dejará de ser válido, obligando al usuario a autenticarse nuevamente.
                .setExpiration(expirationDate)
                //signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET): Firma el JWT usando el
                // algoritmo HS512 (HMAC con SHA-512) y una clave secreta (JWT_SECRET). Esta clave secreta se
                // utiliza para generar la firma del token, garantizando su integridad. Si alguien intenta
                // modificar el contenido del token sin conocer la clave secreta, la firma no coincidirá, y el
                // token será considerado inválido.
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)  // Firmar el token con el algoritmo HS512 y la clave secreta
            //compact(): Finalmente, este método genera el token JWT en formato compacto, es decir, el token como una cadena de
            // texto codificada en Base64, que puede ser enviada en una cabecera HTTP.
                .compact();
        //El token JWT generado se devuelve como una cadena de texto que puede ser utilizado en
        // las respuestas HTTP o almacenado en el cliente.
        return token;
    }

    // el método getUsernameFromJwt toma un JWT, verifica su firma usando una clave secreta y luego extrae y
    //  devuelve el nombre de usuario (sujeto) contenido en el token.
    public String getUsernameFromJwt(String token) {
            //Jwts.parser(): Llama al método estático parser() de la clase Jwts
            // que devuelve un objeto JwtParser. Este objeto se utiliza para analizar el JWT.
            Claims claims = Jwts.parser()
                    //.setSigningKey(SecurityConstants.JWT_SECRET): Configura la clave secreta que se utiliza
                    // para validar la firma del JWT. SecurityConstants.JWT_SECRET debe ser una constante que
                    // contiene la misma clave secreta utilizada para firmar el JWT al generarlo.
                    .setSigningKey(SecurityConstants.JWT_SECRET)
                    //.parseClaimsJws(token): Analiza el JWT proporcionado en el parámetro token y verifica
                    // su firma. Retorna un objeto Jws<Claims>, que contiene las afirmaciones (claims) del JWT si el token
                    // es válido. Si el token no es válido, se lanza una excepción.
                    .parseClaimsJws(token)

                    //.getBody(): Extrae el cuerpo del objeto Jws<Claims>, que es un objeto Claims. Este objeto contiene
                    // los datos del token, como el sujeto (nombre de usuario), fecha de expiración, etc.
                    .getBody();
            //return claims.getSubject();: Obtiene el sujeto (subject) del objeto Claims, que en el contexto de un JWT
        // suele ser el nombre de usuario. Luego devuelve este valor como el resultado del método.
            return claims.getSubject();

    }
    public boolean validateToken(String token) {
        try {

            //Jwts.parser(): Esto crea una instancia de JwtParser, que es una clase proporcionada
            // por la biblioteca jjwt para parsear y validar tokens JWT.

            //setSigningKey(SecurityConstants.JWT_SECRET): Aquí, se establece la clave secreta que se utilizó
            // para firmar el token JWT. La clave secreta se almacena en SecurityConstants.JWT_SECRET. Esta clave
            // se usa para verificar que el token no ha sido alterado desde que fue emitido. Es importante que esta
            // clave coincida con la que se utilizó para crear el token en primer lugar.

            //parseClaimsJws(token): Este método intenta parsear el token JWT y extraer las reclamaciones (claims)
            // contenidas en él. Durante este proceso, también verifica que el token sea válido y no haya sido alterado.
            // Si el token es inválido o ha expirado, se lanzará una excepción.
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);

            //Si el método parseClaimsJws no lanza ninguna excepción, significa que el token ha sido verificado
            // correctamente y es válido. En este caso, se retorna true.
            return true;
        } catch (Exception ex) {

            throw new AuthenticationServiceException("JWT was expired or incorrect");
        }
}

}
