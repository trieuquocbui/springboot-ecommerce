package com.bqt.ecommerce.payloads.response;

import com.bqt.ecommerce.entities.Product;
import com.bqt.ecommerce.entities.Staff;
import lombok.Data;

import java.util.Date;

@Data
public class PriceResponse {
    private Date appliedDate;

    private double newPrice;
}
