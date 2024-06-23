package com.bqt.ecommerce.services;

import com.bqt.ecommerce.payloads.request.DiscountDetailsRequest;
import com.bqt.ecommerce.payloads.response.DiscountResponse;

import java.util.List;

public interface IDiscountDetailsService {

    DiscountResponse updateDiscountDetails(DiscountDetailsRequest discountDetailsRequests, String discountId);
}
