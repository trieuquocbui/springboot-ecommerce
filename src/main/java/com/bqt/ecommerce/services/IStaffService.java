package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.StaffRequest;
import com.bqt.ecommerce.payloads.response.StaffResponse;

public interface IStaffService {
    StaffResponse getStaff(Account account);

    StaffResponse updateStaff(String fileName, StaffRequest infoStaff, Account account);
}
