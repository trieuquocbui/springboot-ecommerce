package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
@Table(name = "order_details")
public class OrderDetails {
    @EmbeddedId
    private OrderDetailsPk orderDetailsPk;

    @ManyToOne
    @MapsId("product")
    @JoinColumn()
    private Product product;

    @ManyToOne
    @MapsId("order")
    @JoinColumn()
    private Order order;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}
