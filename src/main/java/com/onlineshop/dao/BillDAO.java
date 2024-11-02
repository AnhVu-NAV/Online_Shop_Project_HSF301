package com.onlineshop.dao;

import com.onlineshop.dto.BillDetailDTO;
import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;
import java.util.List;

public interface BillDAO {
    List<BillDetailForAdmin> getBillDetailForAdmin();
    List<BillDetail> getBillDetail(int billId);

    List<BillDetailDTO> getBillDetails(int billId);

    void updateBillStatus(String status, int billId);
    List<BillDetailForAdmin> getBillDetailForAdminByStatus(String status);
}