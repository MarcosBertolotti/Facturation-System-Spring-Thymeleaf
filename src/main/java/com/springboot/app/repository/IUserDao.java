package com.springboot.app.repository;

import com.springboot.app.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long> {

    public User findByUsername(String username);
}
