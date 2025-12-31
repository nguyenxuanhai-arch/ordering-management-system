package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String size;
    private String description;
    private BigDecimal price;
    private String category;
    private String imgUrl;
    private Integer quantities;
}

