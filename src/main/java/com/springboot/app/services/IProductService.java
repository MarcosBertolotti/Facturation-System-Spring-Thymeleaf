package com.springboot.app.services;

import com.springboot.app.entities.Product;

import java.util.List;

public interface IProductService {

    List<Product> findByName(String term);

    Product findProductById(Long id);
}
