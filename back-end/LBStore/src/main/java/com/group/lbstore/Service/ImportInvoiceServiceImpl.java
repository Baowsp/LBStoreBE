package com.group.lbstore.Service;
import com.group.lbstore.Model.ImportInvoice;
import com.group.lbstore.Model.ImportInvoiceDetail;
import com.group.lbstore.Model.ImportStatus;
import com.group.lbstore.Service.ImportInvoiceService;
import com.group.lbstore.Repository.ImportInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ImportInvoiceServiceImpl implements ImportInvoiceService {
    private final ImportInvoiceRepository importInvoiceRepository;

    @Override
    public ImportInvoice createInvoice(ImportInvoice importInvoice) {
        if (importInvoice.getInvoiceNumber() == null || importInvoice.getInvoiceNumber().isEmpty()) {
            importInvoice.setInvoiceNumber("IMP-" + System.currentTimeMillis());
        }
        if (importInvoice.getImportInvoiceDetails() != null) {
            for (ImportInvoiceDetail detail : importInvoice.getImportInvoiceDetails()) {
                detail.setImportInvoice(importInvoice);
            }
        }
        importInvoice.setStatus(ImportStatus.DRAFT);
        return importInvoiceRepository.save(importInvoice);
    }
    @Override
    public List<ImportInvoice> getAllInvoices() {
        return importInvoiceRepository.findAll();
    }
    @Override
    public ImportInvoice getInvoiceById(Long id) {
        return importInvoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Import Invoice not found"));
    }
    @Override
    public ImportInvoice updateStatus(Long id, String status) {
        // This should probably use an Enum like ExportStatus
        ImportInvoice invoice = getInvoiceById(id);
        invoice.setStatus(ImportStatus.valueOf(status));
        // TODO: When status is COMPLETED, update product stock.
        return importInvoiceRepository.save(invoice);
    }
}