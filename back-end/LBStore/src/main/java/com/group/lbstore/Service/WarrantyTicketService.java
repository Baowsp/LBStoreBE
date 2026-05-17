package com.group.lbstore.Service;

import com.group.lbstore.Model.WarrantyStatus;
import com.group.lbstore.Model.WarrantyTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WarrantyTicketService {
    WarrantyTicket createTicket(WarrantyTicket ticketRequest);

    Page<WarrantyTicket> findTickets(WarrantyStatus status, Long customerId, Pageable pageable);

    Optional<WarrantyTicket> getTicketById(Long id);

    Optional<WarrantyTicket> updateStatus(Long id, WarrantyStatus newStatus, String notes);

    Optional<WarrantyTicket> updateTicket(Long id, WarrantyTicket ticketDetails);
}