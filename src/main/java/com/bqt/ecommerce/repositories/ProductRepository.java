package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameContaining(Pageable pageable, String search);

    Page<Product> findByStatusTrueAndBrand(Brand brand, Pageable pageable);

    Page<Product> findByStatusTrue(Pageable pageable);

    Page<Product> findAllByLabel(String label,Pageable pageable);

    List<Product> findByNameContaining(String name);

    Optional<Product> findByName(String name);

    @Procedure(value = "DELETE_PRODUCT")
    void deleteByProductId(long productId);
}
