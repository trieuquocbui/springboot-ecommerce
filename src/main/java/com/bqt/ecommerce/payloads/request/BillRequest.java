package com.bqt.ecommerce.payloads.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BillRequest {

    private String province;

    private String district;

    private String ward;

    private String addressHome;

    private String note;

    private double totalAmount;

    private int status;

    private List<BillDetailsRequest> billDetails;

    private UserRequest user;
}
