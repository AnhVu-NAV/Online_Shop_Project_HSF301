package com.onlineshop.dao;

import com.onlineshop.dto.BillDetailDTO;
import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.User;

import java.util.List;

public interface BillDAO {
    List<BillDetailForAdmin> getBillDetailForAdmin();
    List<BillDetail> getBillDetail(int billId);

    List<BillDetailDTO> getBillDetails(int billId);

    void updateBillStatus(String status, int billId);
    List<BillDetailForAdmin> getBillDetailForAdminByStatus(String status);
    int insert(Order order, User user, String status);
    void updateStatus(String status, int billId);
    List<BillDetail> showBillDetail(int billId);
    List<BillDetail> showBillDetailForAdmin();
    List<BillDetail> showBillDetailForAdminFilterByStatus(String status);
}