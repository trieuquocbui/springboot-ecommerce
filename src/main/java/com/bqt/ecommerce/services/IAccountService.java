package com.bqt.ecommerce.services;


import com.bqt.ecommerce.payloads.request.AccountRequest;
import com.bqt.ecommerce.payloads.request.SignUpRequest;
import com.bqt.ecommerce.payloads.request.SignUpStaffRequest;
import com.bqt.ecommerce.payloads.response.AccountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

public interface IAccountService {
    void createAccount(SignUpRequest signUpRequest);

    PaginationResponse<AccountResponse> findAccountOfUser(int page, int limit,String sortDir, String sortBy, String username);

    PaginationResponse<AccountResponse> getUserList(int page, int limit,String sortDir, String sortBy);

    AccountResponse updateStatusAccount(Long idUser, AccountRequest accountRequest);

    PaginationResponse<AccountResponse> getStaffList(int page, int limit,String sortDir, String sortBy);

    void createAccountStaff(SignUpStaffRequest signUpStaffRequest);

    PaginationResponse<AccountResponse> findAccountOfStaff(int page, int limit, String sortDir, String sortBy, String search);

    AccountResponse findAccountById(int accountId);
}
