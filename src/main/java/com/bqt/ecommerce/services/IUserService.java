package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.UserRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.UserResponse;

public interface IUserService {

    UserResponse updateUser(String image, UserRequest infoUser, Account account);

    UserResponse getUser(Account account);

    PaginationResponse getListUser(int page, int limit);
}
