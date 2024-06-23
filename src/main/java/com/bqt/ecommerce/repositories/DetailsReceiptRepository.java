package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.ReceiptDetails;
import com.bqt.ecommerce.entities.ReceiptDetailsPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsReceiptRepository extends JpaRepository<ReceiptDetails, ReceiptDetailsPk> {
}
