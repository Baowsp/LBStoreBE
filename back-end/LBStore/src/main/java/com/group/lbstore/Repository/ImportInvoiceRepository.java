package com.group.lbstore.Repository;

import com.group.lbstore.Model.ImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, Long> {
}