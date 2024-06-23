package com.bqt.ecommerce.payloads;

import com.bqt.ecommerce.payloads.response.OrderDetailsResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;
import com.bqt.ecommerce.payloads.response.StaffResponse;
import com.bqt.ecommerce.payloads.response.SupplierResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderPayloads {
    private Long id;

    private Date date;

    private List<OrderDetailsResponse> orderDetails;
}
