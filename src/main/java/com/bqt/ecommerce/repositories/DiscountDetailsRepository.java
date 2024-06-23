package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.DiscountDetails;
import com.bqt.ecommerce.entities.DiscountDetailsPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DiscountDetailsRepository extends JpaRepository<DiscountDetails, DiscountDetailsPk> {

    @Procedure(value = "DELETE_DISCOUNT_DETAILS")
    void deleteDiscountDetailsByProductIdAndDiscountId(long productId,String discountId);
}
