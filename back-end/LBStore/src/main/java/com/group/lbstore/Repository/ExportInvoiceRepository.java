package com.group.lbstore.Repository;

import com.group.lbstore.Model.ExportInvoice;
import com.group.lbstore.Model.ExportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExportInvoiceRepository extends JpaRepository<ExportInvoice, Long> {
    // Tìm phiếu xuất theo mã phiếu
    Optional<ExportInvoice> findByInvoiceNumber(String invoiceNumber);

    // Lọc phiếu theo trạng thái (VD: DRAFT để nhân viên kho thấy việc cần làm)
    List<ExportInvoice> findByStatus(ExportStatus status);
}