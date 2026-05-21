package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByProductCode(String productCode);

    boolean existsByProductName(String productName);

    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);
}
