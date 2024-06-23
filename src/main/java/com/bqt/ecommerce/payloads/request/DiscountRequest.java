package com.bqt.ecommerce.payloads.request;

import com.bqt.ecommerce.entities.DiscountDetails;
import com.bqt.ecommerce.payloads.response.DiscountDetailsResponse;
import lombok.Data;


import java.util.Date;
import java.util.List;

@Data
public class DiscountRequest {
    private String id;

    private String reason;

    private Date startDay;

    private Date endDay;

    private int discountPercent;

}
