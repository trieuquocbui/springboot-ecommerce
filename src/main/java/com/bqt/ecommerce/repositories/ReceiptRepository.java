package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt,String> {

//    List<Receipt> findByListReceiptDetailsIsNotNullAndDateBetween(Date fromDate,Date toDate);
    List<Receipt> findByDateBetween(Date fromDate,Date toDate);

    Page<Receipt> findByIdContaining(Pageable pageable, String search);
}
