package com.bqt.ecommerce.payloads.request;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationRequest {
    private long id;

    private String title;

    private String body;

    private Date date;

    private boolean looked;

    private boolean status;
}
