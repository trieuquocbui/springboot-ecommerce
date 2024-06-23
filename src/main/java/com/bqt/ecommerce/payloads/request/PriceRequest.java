package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.Product;
import lombok.Data;
import java.util.Date;

@Data
public class PriceRequest {

    //private Product product;

    private Date appliedDate;

    private double newPrice;
}
