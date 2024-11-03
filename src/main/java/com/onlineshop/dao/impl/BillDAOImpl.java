
package com.onlineshop.dao.impl;

import com.onlineshop.dao.BillDAO;
import com.onlineshop.dto.BillDetailDTO;
import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BillDAOImpl implements BillDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<BillDetailForAdmin> getBillDetailForAdmin() {
        Session session = sessionFactory.getCurrentSession();

        // Native SQL query to select the relevant data
        String sql = "SELECT b.id, u.fullname as customerName, b.created_date, u.address, u.email, u.phone, " +
                "SUM(od.product_quantity * p.price) as total, b.status " +
                "FROM bill b " +
                "JOIN order_detail od ON od.order_id = b.order_id " +
                "JOIN product p ON p.id = od.product_id " +
                "JOIN users u ON u.id = b.user_id " +
                "GROUP BY b.id, u.fullname, b.created_date, u.address, u.email, u.phone, b.status";

        List<Object[]> rows = session.createNativeQuery(sql).getResultList();

        // Manually map the results to BillDetailForAdmin DTOs
        return rows.stream().map(row -> {
            BillDetailForAdmin billDetail = new BillDetailForAdmin();
            billDetail.setId((Integer) row[0]);
            billDetail.setCustomerName((String) row[1]);
            billDetail.setCreatedDate((java.sql.Date) row[2]);
            billDetail.setAddress((String) row[3]);
            billDetail.setEmail((String) row[4]);
            billDetail.setPhone((String) row[5]);
            billDetail.setTotal((Double) row[6]);
            billDetail.setStatus((String) row[7]);
            return billDetail;
        }).collect(Collectors.toList());
    }


    @Override
    public List<BillDetail> getBillDetail(int billId) {
        Session session = sessionFactory.getCurrentSession();

        // Native SQL query to select the relevant bill details
        String sql = "SELECT bd.id, p.name as productName, bd.product_quantity, bd.price " +
                "FROM order_detail bd " +
                "JOIN product p ON p.id = bd.product_id " +
                "WHERE bd.bill_id = :billId";

        List<Object[]> rows = session.createNativeQuery(sql)
                .setParameter("billId", billId)
                .getResultList();

        // Manually map the results to BillDetail DTOs
        return rows.stream().map(row -> {
            BillDetail billDetail = new BillDetail();
            billDetail.setId((Integer) row[0]);
            billDetail.setProductName((String) row[1]);
            billDetail.setProductQuantity((Integer) row[2]);
            billDetail.setPrice((Double) row[3]);
            billDetail.setSubTotal(billDetail.getPrice() * billDetail.getProductQuantity());
            return billDetail;
        }).collect(Collectors.toList());
    }



    @Override
    public List<BillDetailDTO> getBillDetails(int billId) {
        Session session = sessionFactory.getCurrentSession();

        String sql = "SELECT b.id as billId, u.fullname as customerName, b.created_date as createdDate, " +
                "p.name as productName, p.image_url, od.product_quantity as quantity, " +
                "p.price, (p.price * od.product_quantity) as total " +
                "FROM bill b " +
                "JOIN users u ON u.id = b.user_id " +
                "JOIN order_detail od ON od.order_id = b.order_id " +
                "JOIN product p ON p.id = od.product_id " +
                "WHERE b.id = :billId";

        List<Object[]> rows = session.createNativeQuery(sql)
                .setParameter("billId", billId)
                .getResultList();

        // Map dữ liệu vào BillDetailDTO
        return rows.stream().map(row -> {
            BillDetailDTO detail = new BillDetailDTO();
            detail.setBillId((Integer) row[0]);
            detail.setCustomerName((String) row[1]);
            detail.setCreatedDate(((java.sql.Date) row[2]).toLocalDate());
            detail.setProductName((String) row[3]);
            detail.setImageUrl((String) row[4]);
            detail.setQuantity((Integer) row[5]);
            detail.setPrice((Double) row[6]);
            detail.setSubTotal((Double) row[7]);
            return detail;
        }).collect(Collectors.toList());
    }
    @Override
    public void updateBillStatus(String status, int billId) {
        Session session = sessionFactory.getCurrentSession();

        // Update the status of the bill
        String sql = "UPDATE bill SET status = :status WHERE id = :billId";
        session.createNativeQuery(sql)
                .setParameter("status", status)
                .setParameter("billId", billId)
                .executeUpdate();
    }

    @Override
    public List<BillDetailForAdmin> getBillDetailForAdminByStatus(String status) {
        Session session = sessionFactory.getCurrentSession();

        // Native SQL query to select the relevant data filtered by statuses
        String sql = "SELECT b.id, u.fullname as customerName, b.created_date, u.address, u.email, u.phone, " +
                "SUM(od.product_quantity * p.price) as total, b.status " +
                "FROM bill b " +
                "JOIN order_detail od ON od.order_id = b.order_id " +
                "JOIN product p ON p.id = od.product_id " +
                "JOIN users u ON u.id = b.user_id " +
                "WHERE b.status = :status " +
                "GROUP BY b.id, u.fullname, b.created_date, u.address, u.email, u.phone, b.status";

        List<Object[]> rows = session.createNativeQuery(sql)
                .setParameter("status", status)
                .getResultList();

        // Manually map the results to BillDetailForAdmin DTOs
        return rows.stream().map(row -> {
            BillDetailForAdmin billDetail = new BillDetailForAdmin();
            billDetail.setId((Integer) row[0]);
            billDetail.setCustomerName((String) row[1]);
            billDetail.setCreatedDate(Date.valueOf(((Date) row[2]).toLocalDate())); // Converting SQL Date to LocalDate
            billDetail.setAddress((String) row[3]);
            billDetail.setEmail((String) row[4]);
            billDetail.setPhone((String) row[5]);
            billDetail.setTotal((Double) row[6]); // Adding total setter
            billDetail.setStatus((String) row[7]);
            return billDetail;
        }).collect(Collectors.toList());
    }

}