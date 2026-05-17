package com.group.lbstore.Controller;

import com.group.lbstore.Model.ExportInvoice;
import com.group.lbstore.Model.ExportStatus;
import com.group.lbstore.Service.ExportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/export-invoices")
@RequiredArgsConstructor
public class ExportInvoiceController {

    private final ExportInvoiceService exportInvoiceService;

    @PostMapping
    public ResponseEntity<ExportInvoice> createExportInvoice(@RequestBody ExportInvoice exportInvoice) {
        // Logic: Tạo phiếu xuất kho mới (thường ở trạng thái DRAFT)
        ExportInvoice created = exportInvoiceService.createInvoice(exportInvoice);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExportInvoice>> getAllExportInvoices(
            @RequestParam(required = false) ExportStatus status) {
        // Logic: Lấy danh sách phiếu xuất, có thể lọc theo trạng thái (DRAFT, APPROVED, EXPORTED...)
        List<ExportInvoice> invoices = exportInvoiceService.findAll(status);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExportInvoice> getExportInvoiceById(@PathVariable Long id) {
        ExportInvoice invoice = exportInvoiceService.findById(id);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ExportInvoice> updateStatus(
            @PathVariable Long id, 
            @RequestParam ExportStatus status) {
        // Logic: Cập nhật trạng thái phiếu. 
        // Ví dụ: Quản lý duyệt (APPROVED) hoặc Kho xác nhận đã xuất (EXPORTED).
        // Khi chuyển sang EXPORTED, cần cập nhật lại tồn kho của sản phẩm.
        ExportInvoice updated = exportInvoiceService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}