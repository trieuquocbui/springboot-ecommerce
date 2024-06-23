package com.bqt.ecommerce.constants;

import lombok.Getter;

@Getter
public enum StatusOrderEnum {

    APPROVE_ORDER(1),
    DISAPPROVE_ORDER(-1),
    NOT_APPROVE_YET_ORDER(0),
    IN_TRANSIT(2),

    DELIVERED(3),

    COMPLETED(4);

    private int value;

    StatusOrderEnum(int value) {
        this.value = value;
    }

}
