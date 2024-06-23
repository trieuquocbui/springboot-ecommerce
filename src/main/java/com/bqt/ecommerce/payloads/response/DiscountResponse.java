package com.bqt.ecommerce.payloads.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DiscountResponse {
    private String id;

    private String reason;

    private Date startDay;

    private Date endDay;

    private int discountPercent;

    private List<DiscountDetailsResponse> discountDetails;
}
