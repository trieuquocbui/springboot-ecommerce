package com.bqt.ecommerce.payloads.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleRequest {
    private int id;
    private String name;
}
