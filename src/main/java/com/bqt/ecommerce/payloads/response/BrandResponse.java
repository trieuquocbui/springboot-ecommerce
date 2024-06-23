package com.bqt.ecommerce.payloads.response;

import lombok.Data;

import java.util.List;

@Data
public class BrandResponse {
    private long id;

    private String name;

    private String img;

    private boolean status;

    private List<ProductResponse> products;
}
