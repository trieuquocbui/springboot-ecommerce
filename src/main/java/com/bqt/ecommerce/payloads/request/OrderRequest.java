package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.Supplier;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Date date;

    private Supplier supplier;

    private List<OrderDetailsRequest> orderDetails;
}
