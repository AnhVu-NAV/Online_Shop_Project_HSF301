package com.onlineshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_date")
    private Date created_date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private String status; // Thuộc tính không được lưu trong DB

    // Custom constructors
    public Order(Date created_date, User user) {
        this.created_date = created_date;
        this.user = user;
    }

    public Order(int id, Date created_date, User user) {
        this.id = id;
        this.created_date = created_date;
        this.user = user;
    }
}
