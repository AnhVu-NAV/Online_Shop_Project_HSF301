package com.onlineshop.entity;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.*;
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
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private int brand_id;
    private String image_url;
    private Date release_date;

    //constructor for insert a new product
    public Product(int quantity, int brand_id, String name, String description, String image_url, double price, Date release_date) {
        this.quantity = quantity;
        this.brand_id = brand_id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.price = price;
        this.release_date = release_date;
    }
}
