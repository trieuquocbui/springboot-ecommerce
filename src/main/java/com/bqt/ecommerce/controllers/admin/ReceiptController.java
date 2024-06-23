package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.ReceiptRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class ReceiptController {

    @Autowired
    private IReceiptService IReceiptService;

    @PostMapping("receipt")
    public ResponseEntity<ReceiptResponse> createReceipt(
            @RequestBody ReceiptRequest receiptRequest,
            @CurrentAccount Account account){

        ReceiptResponse receiptResponse = this.IReceiptService.createReceipt(receiptRequest,account);

        return ResponseEntity.status(HttpStatus.CREATED).body(receiptResponse);
    }

    @GetMapping("receipt")
    public ResponseEntity<PaginationResponse> getReceiptList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){
        PaginationResponse paginationResponse = this.IReceiptService.getReceiptList(page,limit,sortDir,sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("receipt/find")
    public ResponseEntity<PaginationResponse> getFindReceiptList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search){

        PaginationResponse paginationResponse = this.IReceiptService.getFindReceiptList(page,limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("receipt/{receiptId}")
    public ResponseEntity<ReceiptResponse> showReceiptDetails(@PathVariable("receiptId") String receiptId){

        ReceiptResponse response = this.IReceiptService.getById(receiptId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("receipt/{receiptId}")
    public ResponseEntity<ApiResponse> removeReceipt(@PathVariable("receiptId") String receiptId){

        this.IReceiptService.removeReceipt(receiptId);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
