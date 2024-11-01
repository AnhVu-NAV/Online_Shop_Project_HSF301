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
    private String customerName, productName, image_url;
    private Date created_date;
    private double price, subTotal;

}
