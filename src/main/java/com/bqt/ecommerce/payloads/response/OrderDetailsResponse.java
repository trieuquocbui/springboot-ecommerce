package com.bqt.ecommerce.payloads.response;

import lombok.Data;


@Data
public class OrderDetailsResponse {
    private ProductResponse product;

    private int quantity;

    private double price;
}
