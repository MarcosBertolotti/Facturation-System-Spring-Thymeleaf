package com.springboot.app.services;

import com.springboot.app.entities.Client;
import com.springboot.app.entities.Product;
import com.springboot.app.repository.IClientDao;
import com.springboot.app.repository.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //esta basado en el patron de diseño facade, un unico punto de acceso hacia distintos repositorios. dentro de una clase servicio podriamos tener como atributo y podriamos operar con diferentes clases repositorio, ademas evitamos acceder de forma directa a los repositorios dentro de los controladores
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly=true)
    public List<Client> findAll(){

        return clientDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {

        return clientDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Client client){

        clientDao.save(client);
    }

    @Override
    @Transactional(readOnly=true)
    public Client findOne(Long id){

        return clientDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Client fetchByIdWithBills(Long id) {

        return clientDao.fetchByIdWithBills(id);
    }

    @Override
    @Transactional
    public void delete(Long id){

        clientDao.deleteById(id);
    }

}
