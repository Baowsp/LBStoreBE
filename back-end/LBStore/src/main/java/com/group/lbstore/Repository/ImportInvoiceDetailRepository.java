package com.group.lbstore.Repository;

import com.group.lbstore.Model.ImportInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceDetailRepository extends JpaRepository<ImportInvoiceDetail, Long> {
}