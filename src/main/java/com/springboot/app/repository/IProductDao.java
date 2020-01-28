package com.springboot.app.repository;

import com.springboot.app.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductDao extends CrudRepository<Product, Long> {

    @Query("select p from Product p where p.name like %?1%")
    List<Product> findByName(String term);

    List<Product> findByNameLikeIgnoreCase(String term);
}
