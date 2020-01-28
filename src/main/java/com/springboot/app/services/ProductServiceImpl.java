package com.springboot.app.services;

import com.springboot.app.entities.Product;
import com.springboot.app.repository.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private IProductDao productDao;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByName(String term){ // podria estar en clientService?

        return productDao.findByNameLikeIgnoreCase("%"+term+"%");
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {

        return productDao.findById(id).orElse(null);
    }
}
