package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount,String> {
    Page<Discount> findByIdContaining(Pageable pageable, String search);
}
