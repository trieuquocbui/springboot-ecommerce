package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.StatusOrderEnum;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.BillRequest;
import com.bqt.ecommerce.payloads.response.BillResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IBillService;
import com.bqt.ecommerce.constants.PaginationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController("Order-Admin")
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class BillController {

    @Autowired
    private IBillService IBillService;

    @GetMapping("bill/approve")
    public ResponseEntity<PaginationResponse<BillResponse>> showListApprovingOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.APPROVE_ORDER.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("bill/not-approve-yet")
    public ResponseEntity<PaginationResponse<BillResponse>> showListNotApprovingOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.NOT_APPROVE_YET_ORDER.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("bill/disapprove")
    public ResponseEntity<PaginationResponse<BillResponse>> showListDisApprovingOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.DISAPPROVE_ORDER.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("bill/transit")
    public ResponseEntity<PaginationResponse<BillResponse>> showListInTransitOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.IN_TRANSIT.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("bill/delivered")
    public ResponseEntity<PaginationResponse<BillResponse>> watchListDeliveredOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.DELIVERED.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("bill/completed")
    public ResponseEntity<PaginationResponse<BillResponse>> watchListCompletedOrder(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = IBillService.getStatusBillList(page,limit,StatusOrderEnum.COMPLETED.getValue(),sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @PutMapping("bill/{billId}")
    public ResponseEntity<BillResponse> setStatusOrder(
            @PathVariable("billId") Long billId,
            @RequestBody BillRequest billRequest,
            @CurrentAccount Account account){

        BillResponse billResponse = this.IBillService.updateStatusBill(billId,billRequest,account);

        return ResponseEntity.status(HttpStatus.OK).body(billResponse);
    }
}
