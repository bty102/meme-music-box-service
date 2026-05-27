package com.bty.karaoke.mememusicboxservice.repository;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.dto.response.AnnualRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.MonthlyRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    boolean existsByIdAndCreatedByAccount_Email(Long id, String createdByAccountEmail);

    boolean existsByIdAndMemberAccount_Email(Long id, String memberAccountEmail);

    Page<Invoice> findDistinctByRoomOfInvoices_Room_IdAndRoomOfInvoices_IsTransferred(
            Long roomId,
            Boolean isTransferred,
            Pageable pageable
    );

    Page<Invoice> findDistinctByRoomOfInvoices_Room_IdAndRoomOfInvoices_IsTransferredOrderByCreatedAtDesc(
            Long roomId,
            Boolean isTransferred,
            Pageable pageable
    );

    Page<Invoice> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Invoice> findByCreatedByAccount_IdOrderByCreatedAtDesc(
            Long createdByAccountId,
            Pageable pageable
    );

    Page<Invoice> findByMemberAccount_IdOrderByCreatedAtDesc(
            Long memberAccountId,
            Pageable pageable
    );

    @Query("""
                SELECT new com.bty.karaoke.mememusicboxservice.dto.response.MonthlyRevenueStatisticsResponse(
                    DAY(i.createdAt),
                    SUM(i.finalAmount)
                )
                FROM Invoice i
                WHERE YEAR(i.createdAt) = :year
                    AND MONTH(i.createdAt) = :month
                    AND i.status = :status
                GROUP BY DAY(i.createdAt)
                ORDER BY DAY(i.createdAt)
            """)
    List<MonthlyRevenueStatisticsResponse> getMonthlyRevenueStatistics(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("status") InvoiceStatus status
    );

    @Query("""
                SELECT new com.bty.karaoke.mememusicboxservice.dto.response.AnnualRevenueStatisticsResponse(
                    MONTH(i.createdAt),
                    SUM(i.finalAmount)
                )
                FROM Invoice i
                WHERE YEAR(i.createdAt) = :year
                    AND i.status = :status
                GROUP BY MONTH(i.createdAt)
                ORDER BY MONTH(i.createdAt)
            """)
    List<AnnualRevenueStatisticsResponse> getAnnualRevenueStatistics(
            @Param("year") Integer year,
            @Param("status") InvoiceStatus status
    );
}
