package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Bill;
import com.bqt.ecommerce.entities.Product;
import com.bqt.ecommerce.entities.Seri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface SeriRepository extends JpaRepository<Seri,Long> {
    List<Seri> findByReceivingDate(Date date);

    List<Seri> findAllByProduct(Product product);


    @Query(value = "SELECT * FROM Seri s WHERE s.id_product = :productId AND s.sale_date IS NULL ORDER BY RAND() LIMIT :quantity", nativeQuery = true)
    List<Seri> findRandomSerisByProductAndQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

//    @Procedure(value = "COUNT_UNSOLD_SERI")
//    long getCountUnSoldSeriByProductId(long productId);

//    @Procedure(value = "GET_QUANTITY_RAND_SERI")
//    @Transactional
//    List<Seri> getQuantityRandSeriByProductIdAndLimit(long productId, int limit);

//    @Query(value = "SELECT * FROM Seri s WHERE s.id_product = :productId AND s.sale_date IS NULL AND s.account_id = :accountId AND s.bill_id = :billId", nativeQuery = true)

    List<Seri> findBySaleDateIsNullAndProductAndBill(Product idProduct, Bill billId);

}
