package com.springboot.app.repository;

import com.springboot.app.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IClientDao extends PagingAndSortingRepository<Client, Long> {

    List<Client> findAll();

}
