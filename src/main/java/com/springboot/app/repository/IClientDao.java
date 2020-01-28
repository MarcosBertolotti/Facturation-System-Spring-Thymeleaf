package com.springboot.app.repository;

import com.springboot.app.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IClientDao extends PagingAndSortingRepository<Client, Long> {

    List<Client> findAll();

    @Query("select c from Client c " +
            "left join fetch c.bills b " +
            "where c.id = ?1")
    Client fetchByIdWithBills(Long id);

}
