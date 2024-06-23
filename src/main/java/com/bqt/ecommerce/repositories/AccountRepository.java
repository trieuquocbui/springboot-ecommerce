package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByUsername(String username);
    Boolean existsByUsername(String username);

    Page<Account> findByUserNotNullAndUsernameContaining(Pageable pageable,String username);

    Page<Account> findByStaffNotNullAndUsernameContaining(Pageable pageable,String username);

    Page<Account> findByUserNotNull(Pageable pageable);

    Page<Account> findByStaffNotNull(Pageable pageable);


}
