package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.payloads.request.AccountRequest;
import com.bqt.ecommerce.payloads.response.AccountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class AccountController {

    @Autowired
    private IAccountService IAccountService;

    @GetMapping("account/users")
    public ResponseEntity<PaginationResponse<AccountResponse>> getUserList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = this.IAccountService.getUserList(page,limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("account/users/find")
    public ResponseEntity<PaginationResponse<AccountResponse>> findAccountOfUser(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search){

        PaginationResponse paginationResponse = this.IAccountService.findAccountOfUser(page,limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<AccountResponse> findAccount(
            @PathVariable(name = "accountId") int accountId){

        AccountResponse accountResponse = this.IAccountService.findAccountById(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }

    @GetMapping("account/staff/find")
    public ResponseEntity<PaginationResponse<AccountResponse>> findAccountOfStaff(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search){

        PaginationResponse paginationResponse = this.IAccountService.findAccountOfStaff(page,limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("account/staffs")
    public ResponseEntity<PaginationResponse<AccountResponse>> getListStaff(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy){

        PaginationResponse paginationResponse = this.IAccountService.getStaffList(page,limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @PutMapping("account/{accountId}")
    public ResponseEntity<AccountResponse> updateAccountOfUser(
            @PathVariable("accountId") Long userId,
            @RequestBody AccountRequest accountRequest){

        AccountResponse accountResponse = this.IAccountService.updateStatusAccount(userId,accountRequest);

        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }
}
