package com.pokemonreview.api.controllers;


import com.pokemonreview.api.dto.AuthResponseDto;
import com.pokemonreview.api.dto.LoginDto;
import com.pokemonreview.api.dto.RegisterDto;

import com.pokemonreview.api.models.UserEntity;
import com.pokemonreview.api.repository.RoleRepository;
import com.pokemonreview.api.repository.UserRepository;
import com.pokemonreview.api.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokemonreview.api.models.Role;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {

        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){

        // Verificar si el nombre de usuario ya existe
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        // Crear una nueva entidad de usuario
        UserEntity user=new UserEntity();
        user.setUsername(registerDto.getUsername());
        // Codificar la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Buscar el rol "USER" desde el repositorio de roles

        Role roles = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));

        // Asignar el rol al usuario
       user.setRoles(Collections.singletonList(roles));

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){

        // Crear un objeto de autenticación con las credenciales del usuario, este objeto es esencial para
        // gestionar y verificar los permisos del usuario en el resto de la aplicación.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        // Establecer el contexto de seguridad con la autenticación. Una vez que el usuario ha sido
        // autenticado, es crucial establecer este contexto de seguridad en el hilo actual de la
        // aplicación para que otros componentes  (como servicios, controladores, etc.) de la aplicación
        // puedan acceder a la información del usuario autenticado y verifiquen sus permisos.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Una vez que el usuario ha sido autenticado, se utiliza un JWTGenerator para crear un
        // token JWT basado en la autenticación. Este token es único para el usuario y se puede
        // utilizar en futuras solicitudes para identificar al usuario sin necesidad de volver
        // a ingresar las credenciales.
        String token= jwtGenerator.generateToken(authentication);
        // Finalmente, el token JWT generado se devuelve en un objeto AuthResponseDto, dentro de una
        // respuesta HTTP con el estado OK. El cliente podrá usar este token en futuras solicitudes
        // para acceder a recursos protegidos de la API.
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);


    }


}
