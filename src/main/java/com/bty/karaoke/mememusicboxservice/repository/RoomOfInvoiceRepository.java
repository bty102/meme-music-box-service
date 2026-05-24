package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.entity.RoomOfInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomOfInvoiceRepository extends JpaRepository<RoomOfInvoice, Long> {
    boolean existsByRoom_IdAndInvoice_Id(Long roomId, Long invoiceId);

    boolean existsByInvoice_IdAndIsTransferred(Long invoiceId, Boolean isTransferred);
}
