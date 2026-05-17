package com.group.lbstore.Service;

import com.group.lbstore.Model.ImportInvoiceDetail;
import com.group.lbstore.Repository.ImportInvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImportInvoiceDetailService {

    @Autowired
    private ImportInvoiceDetailRepository importInvoiceDetailRepository;

    public List<ImportInvoiceDetail> findAll() {
        return importInvoiceDetailRepository.findAll();
    }

    public Optional<ImportInvoiceDetail> findById(Long id) {
        return importInvoiceDetailRepository.findById(id);
    }

    public ImportInvoiceDetail save(ImportInvoiceDetail importInvoiceDetail) {
        return importInvoiceDetailRepository.save(importInvoiceDetail);
    }

    public void delete(Long id) {
        importInvoiceDetailRepository.deleteById(id);
    }
}