package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Price;
import com.bqt.ecommerce.entities.PricePk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Date;

public interface PriceRepository extends JpaRepository<Price, PricePk> {

    @Procedure(value = "DELETE_PRICE")
    void deletePriceByDateAndProductId(Date date, long productId);
}
