package com.bqt.ecommerce.payloads.request;

import lombok.Data;

@Data
public class PredictRequest {
    private double price;
    private String job;
    private String brand;
    private String description;
}
