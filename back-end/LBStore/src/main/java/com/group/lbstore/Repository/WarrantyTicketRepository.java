package com.group.lbstore.Repository;

import com.group.lbstore.Model.WarrantyStatus;
import com.group.lbstore.Model.WarrantyTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyTicketRepository extends JpaRepository<WarrantyTicket, Long> {
    
    Page<WarrantyTicket> findByStatus(WarrantyStatus status, Pageable pageable);
    
    Page<WarrantyTicket> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<WarrantyTicket> findByStatusAndCustomerId(WarrantyStatus status, Long customerId, Pageable pageable);
}