package com.group.lbstore.Service;

import com.group.lbstore.Model.ExportInvoice;
import com.group.lbstore.Model.ExportStatus;
import java.util.List;

public interface ExportInvoiceService {
    ExportInvoice createInvoice(ExportInvoice exportInvoice);
    List<ExportInvoice> findAll(ExportStatus status);
    ExportInvoice findById(Long id);
    ExportInvoice updateStatus(Long id, ExportStatus status);
}