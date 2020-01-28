package com.springboot.app.services;

import com.springboot.app.entities.Bill;

public interface IBillService {

    void saveBill(Bill bill);

    Bill findBillById(Long id);

    void deleteBill(Long id);

    Bill fetchByIdWithClientWhitItemBillWithProduct(Long id);
}
