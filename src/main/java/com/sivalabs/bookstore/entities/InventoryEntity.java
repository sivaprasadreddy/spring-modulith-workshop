package com.sivalabs.bookstore.entities;

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "inventory", schema = "inventory")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "inventory_id_generator")
    @SequenceGenerator(name = "inventory_id_generator", sequenceName = "inventory_id_seq", schema = "catalog")
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true)
    @NotEmpty(message = "Product code is required") private String productCode;

    @Column(nullable = false)
    private Long quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
