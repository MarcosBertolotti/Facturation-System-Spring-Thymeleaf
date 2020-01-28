package com.springboot.app.services;

import com.springboot.app.entities.Bill;
import com.springboot.app.repository.IBillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements IBillService{

    @Autowired
    private IBillDao billDao;

    @Override
    @Transactional
    public void saveBill(Bill bill) {

        billDao.save(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public Bill findBillById(Long id) {

        return billDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteBill(Long id) {

        billDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Bill fetchByIdWithClientWhitItemBillWithProduct(Long id) {

        return billDao.fetchByIdWithClientWhitItemBillWithProduct(id);
    }


}
