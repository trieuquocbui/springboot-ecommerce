package com.bqt.ecommerce.payloads.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private Date date;

    private SupplierResponse supplier;

    private StaffResponse staff;

    private ReceiptResponse receipt;

    private List<OrderDetailsResponse> orderDetails;
}
