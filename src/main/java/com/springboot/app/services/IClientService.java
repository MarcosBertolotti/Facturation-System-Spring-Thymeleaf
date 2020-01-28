package com.springboot.app.services;

import com.springboot.app.entities.Client;
import com.springboot.app.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {

    List<Client> findAll();

    Page<Client> findAll(Pageable pageable); // page = iterable. subconjunto con la cant de registros de la pag actual.

    void save(Client client);

    Client findOne(Long id);

    Client fetchByIdWithBills(Long id);

    void delete(Long id);

}
