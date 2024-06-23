package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.DiscountRequest;
import com.bqt.ecommerce.payloads.response.DiscountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

public interface IDiscountService {
    DiscountResponse createDiscount(DiscountRequest discountRequest, Account account);

    DiscountResponse findDiscount(String discountId);

    void deleteDiscount(String discountId);

    PaginationResponse<DiscountResponse> getDiscountList(int page, int limit,String sortDir, String sortBy);

    DiscountResponse updateDiscount(String discountId, DiscountRequest discountRequest);

    PaginationResponse<DiscountResponse> findDiscountList(int page, int limit, String sortDir, String sortBy, String search);
}
