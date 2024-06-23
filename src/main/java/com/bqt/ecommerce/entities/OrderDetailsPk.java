package com.bqt.ecommerce.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDetailsPk implements Serializable {

    private long product;

    private long order;
}
