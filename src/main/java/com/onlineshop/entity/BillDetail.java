package com.onlineshop.entity;

import java.sql.Date;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BillDetail {
    private int id, productQuantity;
    private String customerName, productName, imageUrl;
    private Date createdDate;
    private double price, subTotal;

}