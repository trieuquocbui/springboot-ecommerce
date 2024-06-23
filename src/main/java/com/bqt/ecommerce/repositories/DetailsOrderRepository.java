package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.OrderDetails;
import com.bqt.ecommerce.entities.OrderDetailsPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsOrderRepository extends JpaRepository<OrderDetails, OrderDetailsPk> {
}
