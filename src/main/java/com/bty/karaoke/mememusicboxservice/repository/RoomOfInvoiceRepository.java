package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.entity.RoomOfInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomOfInvoiceRepository extends JpaRepository<RoomOfInvoice, Long> {
    boolean existsByRoom_IdAndInvoice_Id(Long roomId, Long invoiceId);

    boolean existsByInvoice_IdAndIsTransferred(Long invoiceId, Boolean isTransferred);

    boolean existsByInvoice_IdAndRoom_IdAndIsTransferred(Long invoiceId, Long roomId, Boolean isTransferred);

    Optional<RoomOfInvoice> findByInvoice_IdAndIsTransferred(Long invoiceId, Boolean isTransferred);

    Optional<RoomOfInvoice> findByRoom_IdAndIsTransferred(Long roomId, Boolean isTransferred);

    Optional<RoomOfInvoice> findByRoom_IdAndInvoice_StatusAndIsTransferred(Long roomId, InvoiceStatus invoiceStatus, Boolean isTransferred);

    List<RoomOfInvoice> findByInvoice_Id(Long invoiceId);
}
