package com.onlineshop.service.impl;

import com.onlineshop.dao.BillDAO;
import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;
import com.onlineshop.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillDAO billDAO;

    @Override
    @Transactional
    public List<BillDetailForAdmin> showBillDetailForAdmin() {
        return billDAO.getBillDetailForAdmin();
    }

    @Override
    @Transactional
    public List<BillDetail> showBillDetail(int billId) {
        return billDAO.getBillDetail(billId);
    }

    @Override
    @Transactional
    public void updateStatus(String status, int billId) {
        billDAO.updateBillStatus(status, billId);
    }

    @Override
    @Transactional
    public List<BillDetailForAdmin> showBillDetailForAdminFilterByStatus(String status) {
        return billDAO.getBillDetailForAdminByStatus(status);
    }
}

