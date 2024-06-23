package com.bqt.ecommerce.payloads.response;

import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.payloads.ConfigPayLoad;
import lombok.Data;

@Data
public class ProductResponse {
    private long id;

    private String img;

    private String label;

    private String name;

    private String description;

    private boolean status;

    private ConfigPayLoad config;

    private Brand brand;

    private int rate;

    private double price;

}
