package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Price {

    @EmbeddedId
    private PricePk pricePk;

    @ManyToOne
    @MapsId("product")
    @JoinColumn()
    private Product product;

    @ManyToOne
    @MapsId("staff")
    @JoinColumn()
    private Staff staff;

    private double newPrice;
}
