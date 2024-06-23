package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class DiscountDetailsPk implements Serializable {

    private static final long serialVersionUID = 4986765201395516390L;

    private long product;

    private String discount;
}
