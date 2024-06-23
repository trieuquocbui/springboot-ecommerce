package com.bqt.ecommerce.payloads.response;

import com.bqt.ecommerce.payloads.OrderPayloads;
import lombok.Data;

import java.util.List;

@Data
public class SupplierResponse {
    private String id;

    private String name;

    private String phoneNumber;

    private String address;

    private boolean status;

    private List<OrderPayloads> orders;
}
