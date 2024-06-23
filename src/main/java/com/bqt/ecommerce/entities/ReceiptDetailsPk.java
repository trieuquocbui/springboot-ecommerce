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
public class ReceiptDetailsPk implements Serializable {
    private String receipt;

    private OrderDetailsPk orderDetailsPk;
}
