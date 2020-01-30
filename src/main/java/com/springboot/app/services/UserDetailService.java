package com.springboot.app.services;

import com.springboot.app.entities.User;
import com.springboot.app.repository.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("UserDetailService")
public class UserDetailService implements UserDetailsService { // interfaz propia de spring security para trabajar el proceso de login con jpa/jdbc.

    @Autowired
    private IUserDao userDao;

    private Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);

        if(user == null){
            logger.error("Error login: '" + username + "' user doesn't exists");
            throw new UsernameNotFoundException("Username '" + username + "' doesn't exists in the system!");
        }

        List<GrantedAuthority> authorities;
/*
        for(Role role: user.getRoles()){
            logger.info("Role: " + role.getAuthority());
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
*/
        authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        if(authorities.isEmpty()){
            logger.error("Error login: '" + username + "' user hasn't roles assigned!");
            throw new UsernameNotFoundException("Error login: '" + username + "' user hasn't roles assigned!");
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.getEnabled(), true, true, true, authorities);
    }
}
