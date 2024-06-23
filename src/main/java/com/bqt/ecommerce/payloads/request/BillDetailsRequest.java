package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.payloads.response.ProductResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BillDetailsRequest {

    private ProductResponse product;

    private int quantity;

}
