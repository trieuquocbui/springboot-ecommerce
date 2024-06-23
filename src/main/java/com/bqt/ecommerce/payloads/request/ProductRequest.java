package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.entities.Config;
import com.bqt.ecommerce.payloads.ConfigPayLoad;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private String img;

    private String name;

    private String label;

    private double price;

    private String description;

    private ConfigPayLoad config;

    private Brand brand;

}
