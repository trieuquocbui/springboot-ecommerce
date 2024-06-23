package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.PriceRequest;
import com.bqt.ecommerce.payloads.response.PriceResponse;

public interface IPriceService {
    PriceResponse createPriceForProduct(PriceRequest priceRequest, long productId, Account account);
}
