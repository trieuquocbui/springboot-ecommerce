package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.DiscountDetailsRequest;
import com.bqt.ecommerce.payloads.request.DiscountRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.DiscountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IDiscountDetailsService;
import com.bqt.ecommerce.services.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class DiscountController {
    @Autowired
    private IDiscountService discountService;

    @Autowired
    private IDiscountDetailsService discountDetailsService;

    @GetMapping("discount/{discountId}")
    public ResponseEntity<DiscountResponse> findDiscount(@PathVariable("discountId") String discountId) {

        DiscountResponse discountResponse = this.discountService.findDiscount(discountId);

        return ResponseEntity.status(HttpStatus.OK).body(discountResponse);
    }

    @PostMapping("discount")
    public ResponseEntity<DiscountResponse> createDiscount(
            @RequestBody DiscountRequest discountRequest,
            @CurrentAccount Account account) {

        DiscountResponse discountResponse = this.discountService.createDiscount(discountRequest, account);

        return ResponseEntity.status(HttpStatus.CREATED).body(discountResponse);
    }

    @PutMapping("discount/{discountId}/products")
    public ResponseEntity<DiscountResponse> updateDiscountDetails(
            @PathVariable(name = "discountId") String discountId,
            @RequestBody DiscountDetailsRequest discountDetailsRequests) {

        DiscountResponse discountResponse = this.discountDetailsService.updateDiscountDetails(discountDetailsRequests, discountId);

        return ResponseEntity.status(HttpStatus.OK).body(discountResponse);
    }

    @DeleteMapping("discount/{discountId}")
    public ResponseEntity<ApiResponse> deleteDiscount(@PathVariable("discountId") String discountId) {

        this.discountService.deleteDiscount(discountId);
        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("discount")
    public ResponseEntity<PaginationResponse<DiscountResponse>> getDiscount(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy) {

        PaginationResponse paginationResponse = this.discountService.getDiscountList(page, limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("discount/find")
    public ResponseEntity<PaginationResponse<DiscountResponse>> findDiscount(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search) {

        PaginationResponse paginationResponse = this.discountService.findDiscountList(page, limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @PutMapping("discount/{discountId}")
    public ResponseEntity<DiscountResponse> updateDiscount(
            @PathVariable("discountId") String discountId,
            @RequestBody DiscountRequest discountRequest) {

        DiscountResponse discountResponse = this.discountService.updateDiscount(discountId, discountRequest);

        return ResponseEntity.status(HttpStatus.OK).body(discountResponse);
    }
}
