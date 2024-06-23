package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.OrderRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.OrderResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("ORDER-ADMIN")
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("order/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId){

        OrderResponse orderResponse = orderService.findOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PostMapping("order")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest orderRequest,
            @CurrentAccount Account userPrincipal){
        OrderResponse orderResponse = orderService.createOrder(orderRequest,userPrincipal);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @DeleteMapping("order/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId){

        orderService.deleteOrder(orderId);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("order")
    public ResponseEntity<PaginationResponse> getOrderList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = orderService.getOrderList(page,limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("order/all")
    public ResponseEntity<List<OrderResponse>> getAllOrder(){

        List<OrderResponse> orderResponses = orderService.getAllOrder();

        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }
}
