package com.group.lbstore.Controller;

import com.group.lbstore.Model.ImportInvoice;
import com.group.lbstore.Service.ImportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/import-invoices")
@RequiredArgsConstructor
public class ImportInvoiceController {

    private final ImportInvoiceService importInvoiceService;

    @PostMapping
    public ResponseEntity<ImportInvoice> createImportInvoice(@RequestBody ImportInvoice importInvoice) {
        ImportInvoice created = importInvoiceService.createInvoice(importInvoice);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ImportInvoice>> getAllImportInvoices() {
        List<ImportInvoice> invoices = importInvoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportInvoice> getImportInvoiceById(@PathVariable Long id) {
        ImportInvoice invoice = importInvoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    // API duyệt phiếu nhập hoặc cập nhật trạng thái thanh toán
    @PatchMapping("/{id}/status")
    public ResponseEntity<ImportInvoice> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ImportInvoice updated = importInvoiceService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}