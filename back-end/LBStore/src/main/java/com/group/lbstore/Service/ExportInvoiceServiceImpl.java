package com.group.lbstore.Service;

import com.group.lbstore.Model.ExportInvoice;
import com.group.lbstore.Model.ExportStatus;
import com.group.lbstore.Repository.ExportInvoiceRepository;
import com.group.lbstore.Service.ExportInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportInvoiceServiceImpl implements ExportInvoiceService {

    private final ExportInvoiceRepository exportInvoiceRepository;

    @Override
    public ExportInvoice createInvoice(ExportInvoice exportInvoice) {
        return exportInvoiceRepository.save(exportInvoice);
    }

    @Override
    public List<ExportInvoice> findAll(ExportStatus status) {
        return status != null ? exportInvoiceRepository.findByStatus(status) : exportInvoiceRepository.findAll();
    }

    @Override
    public ExportInvoice findById(Long id) {
        return exportInvoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public ExportInvoice updateStatus(Long id, ExportStatus status) {
        ExportInvoice invoice = findById(id);
        invoice.setStatus(status);
        return exportInvoiceRepository.save(invoice);
    }
}