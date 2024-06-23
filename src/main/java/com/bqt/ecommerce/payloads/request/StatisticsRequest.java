package com.bqt.ecommerce.payloads.request;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticsRequest {
    private Date fromDate;
    private Date toDate;
}
