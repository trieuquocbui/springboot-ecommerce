package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.Order;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReceiptRequest {
    private String id;
    private Date date;
    private Order order;
    private List<ReceiptDetailsRequest> listReceiptDetail;
}
