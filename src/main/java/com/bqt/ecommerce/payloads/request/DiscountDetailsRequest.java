package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.Discount;
import com.bqt.ecommerce.entities.Product;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import lombok.Data;

import java.util.List;

@Data
public class DiscountDetailsRequest {

    private List<ProductResponse> products;
}
