package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "receipt_details")
public class ReceiptDetails {
    @EmbeddedId
    private ReceiptDetailsPk id;

    @ManyToOne
    @MapsId("receipt")
    @JoinColumn()
    private Receipt receipt;

    @ManyToOne
    @MapsId("orderDetailsPk")
    @JoinColumns({
            @JoinColumn(name = "order", referencedColumnName = "order_id"),
            @JoinColumn(name = "product", referencedColumnName = "product_id")
    })
    private OrderDetails orderDetails;

    private int quantity;

    private double price;

}
