package com.bqt.ecommerce.controllers.user;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.BillRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.BillResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class BillController {
    @Autowired
    private IBillService IBillService;

    @PostMapping("bill")
    public ResponseEntity<BillResponse> billOfUser(
            @RequestBody BillRequest billRequest,
            @CurrentAccount Account account){

        BillResponse billResponse = this.IBillService.createBill(billRequest,account);

        return ResponseEntity.status(HttpStatus.OK).body(billResponse);
    }

    @GetMapping("bill/list")
    public ResponseEntity<PaginationResponse<BillResponse>> showOrderList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @CurrentAccount Account account){

        PaginationResponse paginationResponse = this.IBillService.getBillList(page,limit,sortDir,sortBy,account);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @PutMapping("bill/{billId}/finish")
    public ResponseEntity<BillResponse> finishOrder(
            @PathVariable(name = "billId") Long billId,
            @RequestBody BillRequest billRequest,
            @CurrentAccount Account account){

        BillResponse billResponse = this.IBillService.finishBill(billId,billRequest,account);

        return ResponseEntity.status(HttpStatus.OK).body(billResponse);
    }

    @DeleteMapping("bill/{billId}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(
            @PathVariable(name = "billId") Long billId,
            @CurrentAccount Account account){

        this.IBillService.cancelBill(billId,account);
        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
