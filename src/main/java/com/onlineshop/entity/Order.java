package com.onlineshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;


@Table(name = "orders")
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "total_amount")  // Thêm cột này vào cơ sở dữ liệu nếu chưa có
    private Double totalAmount; // Hoặc sử dụng BigDecimal nếu cần độ chính xác cao hơn

    @Transient
    private String status; // Thuộc tính không được lưu trong DB


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    // Custom constructors
    public Order(Date createdDate, User user) {
        this.createdDate = createdDate;
        this.user = user;
    }

    public Order() {
        this.totalAmount = 0.0; // Gán giá trị mặc định
    }


}