package com.onlineshop.entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BillDetailForAdmin {
    private int id;
    private String customerName;
    private Date created_date;
    private String address;
    private String email;
    private String phone;
    private double total;
    private String status;
}
