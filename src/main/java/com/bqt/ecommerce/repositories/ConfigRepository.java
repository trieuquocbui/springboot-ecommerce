package com.bqt.ecommerce.repositories;

import com.bqt.ecommerce.entities.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config,Long> {
    Optional<Config> findByNameConfig(String nameConfig);

    Page<Config> findByNameConfigContaining(Pageable pageable,String search);


}
