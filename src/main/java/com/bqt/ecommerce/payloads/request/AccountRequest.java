package com.bqt.ecommerce.payloads.request;

import lombok.Data;

@Data
public class AccountRequest {
    private long id;
    private String username;
    private boolean status;
    private RoleRequest role;
}
