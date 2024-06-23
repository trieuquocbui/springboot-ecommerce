package com.bqt.ecommerce.repositories;


import com.bqt.ecommerce.entities.Bill;
import com.bqt.ecommerce.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {
    Page findByUser(User user, Pageable pageable);

    Page findAllByStatus(Pageable pageable, int status);

    @Procedure(value = "delete_bill")
    void deleteBill(Long id);

    List<Bill> findByStatusAndOrderDayBetween(int status,Date fromDate, Date toDate);
}
