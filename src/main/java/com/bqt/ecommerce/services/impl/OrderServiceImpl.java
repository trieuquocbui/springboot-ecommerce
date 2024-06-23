package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.*;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.OrderDetailsRequest;
import com.bqt.ecommerce.payloads.request.OrderRequest;
import com.bqt.ecommerce.payloads.response.OrderResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.repositories.OrderDetailsRepository;
import com.bqt.ecommerce.repositories.OrderRepository;
import com.bqt.ecommerce.repositories.SupplierRepository;
import com.bqt.ecommerce.services.IOrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, Account userPrincipal) {

        Order createdOrder = new Order();

        createdOrder.setStaff(userPrincipal.getStaff());

        createdOrder.setSupplier(orderRequest.getSupplier());

        createdOrder.setDate(orderRequest.getDate());

        Order newOrder = this.orderRepository.save(createdOrder);

        for (OrderDetailsRequest orderDetailsRequest : orderRequest.getOrderDetails()){
            OrderDetails createdOrderDetails = new OrderDetails();
            OrderDetailsPk orderDetailsPk = new OrderDetailsPk();
            orderDetailsPk.setOrder(createdOrder.getId());
            orderDetailsPk.setProduct(orderDetailsRequest.getProduct().getId());
            createdOrderDetails.setOrderDetailsPk(orderDetailsPk);
            createdOrderDetails.setQuantity(orderDetailsRequest.getQuantity());
            createdOrderDetails.setPrice(orderDetailsRequest.getPrice());
            createdOrderDetails.setOrder(createdOrder);
            createdOrderDetails.setProduct(orderDetailsRequest.getProduct());
            OrderDetails newOrderDetails = orderDetailsRepository.save(createdOrderDetails);
        }

        return this.modelMapper.map(newOrder,OrderResponse.class);
    }

    @Override
    public OrderResponse findOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        return this.modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        if (order.getReceipt() == null){
            this.orderRepository.delete(order);
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }

    }

    @Override
    public PaginationResponse getOrderList(int page, int limit,String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page orderPage = this.orderRepository.findAll(pageable);

        List<Brand> pageContent = orderPage.getContent();

        Type listType = new TypeToken<List<OrderResponse>>(){}.getType();

        List<OrderResponse> orderResponses = modelMapper.map(pageContent,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(orderResponses);
        paginationResponse.setLastPage(orderPage.isLast());
        paginationResponse.setPageNumber(orderPage.getNumber());
        paginationResponse.setPageSize(orderPage.getSize());
        paginationResponse.setTotalElements(orderPage.getTotalElements());
        paginationResponse.setTotalPages(orderPage.getTotalPages());
        return paginationResponse;
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<Order> orders = this.orderRepository.findAll();
        Type listType = new TypeToken<List<OrderResponse>>(){}.getType();

        List<OrderResponse> orderResponses = modelMapper.map(orders,listType);

        return orderResponses;
    }
}
