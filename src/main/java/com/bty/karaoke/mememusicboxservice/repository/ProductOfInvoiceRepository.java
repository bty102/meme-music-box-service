package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.ProductOfInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOfInvoiceRepository extends JpaRepository<ProductOfInvoice, Long> {
    boolean existsByProduct_IdAndInvoice_Id(Long productId, Long invoiceId);

    boolean existsByIdAndInvoice_CreatedByAccount_Email(Long id, String invoiceCreatedByAccountEmail);

    List<ProductOfInvoice> findByInvoice_Id(Long invoiceId);
}
