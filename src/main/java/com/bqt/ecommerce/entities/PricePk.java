package com.bqt.ecommerce.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PricePk implements Serializable {
    private long product;

    private long staff;

    @Temporal(TemporalType.DATE)
    private Date appliedDate;
}
