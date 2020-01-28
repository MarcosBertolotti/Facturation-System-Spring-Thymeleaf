package com.springboot.app.repository;

import com.springboot.app.entities.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IBillDao extends CrudRepository<Bill, Long> {

    @Query("select b from Bill b " +
            "join fetch b.client c " +
            "join fetch b.items l " +
            "join fetch l.product " +
            "where b.id = ?1")
    Bill fetchByIdWithClientWhitItemBillWithProduct(Long id);
}
