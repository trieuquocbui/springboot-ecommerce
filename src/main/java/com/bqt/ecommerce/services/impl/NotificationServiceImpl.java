package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.entities.Notification;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.NotificationRequest;
import com.bqt.ecommerce.payloads.response.BrandResponse;
import com.bqt.ecommerce.payloads.response.NotificationResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.repositories.NotificationRepository;
import com.bqt.ecommerce.services.INotificationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PaginationResponse getNotificationListOfUser(int page, int limit, String sortDir, String sortBy, Account account) {

        Pageable pageable = PageRequest.of(page - 1,limit, Sort.by(Sort.Direction.DESC, sortBy));

        Page notificationPage = this.notificationRepository.findByAccountAndStatus(pageable,account,true);

        Type listType = new TypeToken<List<NotificationResponse>>(){}.getType();

        List<NotificationResponse> notificationResponses = modelMapper.map(notificationPage.getContent(),listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(notificationResponses);
        paginationResponse.setLastPage(notificationPage.isLast());
        paginationResponse.setPageNumber(notificationPage.getNumber());
        paginationResponse.setPageSize(notificationPage.getSize());
        paginationResponse.setTotalElements(notificationPage.getTotalElements());
        paginationResponse.setTotalPages(notificationPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public NotificationResponse update(Long notificationId, NotificationRequest notificationRequest, Account account) {
        Notification notification = this.notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

        if(notification.isStatus() != notificationRequest.isStatus()){
            notification.setStatus(false);
        }

        if(notification.isLooked() != notificationRequest.isLooked()){
            notification.setLooked(true);
        }

        Notification saveNotification = this.notificationRepository.save(notification);

        return this.modelMapper.map(saveNotification,NotificationResponse.class);
    }
}
