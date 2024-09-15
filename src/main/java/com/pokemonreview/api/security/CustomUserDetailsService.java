package com.pokemonreview.api.security;

import com.pokemonreview.api.models.Role;
import com.pokemonreview.api.models.UserEntity;
import com.pokemonreview.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user= userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usernamme not found"));
    return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

    }
    
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {

        //Se llama a .stream() para crear un stream de la lista de roles. Un stream es una secuencia de
        // elementos que permite realizar operaciones de forma funcional, como mapeo y filtrado.

        //map() es una operación intermedia de los streams en Java que permite transformar cada
        // elemento del stream en otro objeto.

        //Role se transforma en una instancia de SimpleGrantedAuthority

        //Después de mapear cada rol a una autoridad, usamos collect() para recolectar el resultado en una
        // lista.

        //Collectors.toList() es el recopilador que transforma el stream en una lista de objetos
        // GrantedAuthority. El resultado será un List<GrantedAuthority>.

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
