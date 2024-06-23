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
@Table(name = "discount_details")
public class DiscountDetails {
    @EmbeddedId
    private DiscountDetailsPk discountDetailsPk;

    @ManyToOne
    @MapsId("product")
    @JoinColumn()
    private Product product;

    @ManyToOne
    @MapsId("discount")
    @JoinColumn()
    private Discount discount;
}
