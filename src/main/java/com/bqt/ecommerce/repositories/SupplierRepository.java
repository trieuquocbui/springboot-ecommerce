package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier,String> {
    List<Supplier> findAllByStatus(boolean b);

    Page<Supplier> findByNameContaining(Pageable pageable,String search);
}
