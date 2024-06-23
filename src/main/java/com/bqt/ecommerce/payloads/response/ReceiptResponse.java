package com.bqt.ecommerce.payloads.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReceiptResponse {
    private String id;

    private Date date;

    private List<ReceiptDetailsResponse> listReceiptDetails;
}
