package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.OrderRequest;
import com.bqt.ecommerce.payloads.response.OrderResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderRequest orderRequest, Account userPrincipal);

    OrderResponse findOrder(Long orderId);

    void deleteOrder(Long orderId);

    PaginationResponse getOrderList(int page, int limit,String sortDir, String sortBy);

    List<OrderResponse> getAllOrder();
}
