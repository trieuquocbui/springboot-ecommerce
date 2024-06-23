package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Notification;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page findByAccountAndStatus(Pageable pageable, Account account, boolean status);
}
