package com.bqt.ecommerce.payloads.response;

import com.bqt.ecommerce.payloads.request.BillDetailsRequest;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class BillResponse {
    private long id;

    private Date orderDay;

    private double totalAmount;

    private int status;

    private String addressHome;

    private String province;

    private String district;

    private String ward;

    private String note;

    private List<BillDetailsRequest> billDetails;

    private UserResponse user;
}
