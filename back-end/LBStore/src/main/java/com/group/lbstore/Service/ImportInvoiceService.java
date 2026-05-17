package com.group.lbstore.Service;
import com.group.lbstore.Model.ImportInvoice;
import java.util.List;
public interface ImportInvoiceService {
    ImportInvoice createInvoice(ImportInvoice importInvoice);
    List<ImportInvoice> getAllInvoices();
    ImportInvoice getInvoiceById(Long id);
    ImportInvoice updateStatus(Long id, String status);
}