package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.NotificationRequest;
import com.bqt.ecommerce.payloads.response.NotificationResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

public interface INotificationService {
    PaginationResponse getNotificationListOfUser(int page, int limit,String sortDir, String sortBy, Account account);

    NotificationResponse update(Long notificationId, NotificationRequest notificationRequest, Account account);
}
