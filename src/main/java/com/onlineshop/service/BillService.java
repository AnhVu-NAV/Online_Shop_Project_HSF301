package com.onlineshop.service;

import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;

import java.util.List;

public interface BillService {
    List<BillDetailForAdmin> showBillDetailForAdmin();
    List<BillDetail> showBillDetail(int billId);
    void updateStatus(String status, int billId);
    List<BillDetailForAdmin> showBillDetailForAdminFilterByStatus(String status);
}