package com.group.lbstore.Service;

import com.group.lbstore.Model.WarrantyStatus;
import com.group.lbstore.Model.WarrantyTicket;
import com.group.lbstore.Repository.WarrantyTicketRepository;
import com.group.lbstore.Service.WarrantyTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarrantyTicketServiceImpl implements WarrantyTicketService {

    private final WarrantyTicketRepository warrantyTicketRepository;

    @Override
    public WarrantyTicket createTicket(WarrantyTicket ticketRequest) {
        // Logic xử lý nghiệp vụ (gán ticketNumber, validate...)
        return warrantyTicketRepository.save(ticketRequest);
    }

    @Override
    public Page<WarrantyTicket> findTickets(WarrantyStatus status, Long customerId, Pageable pageable) {
        if (status != null && customerId != null) {
            return warrantyTicketRepository.findByStatusAndCustomerId(status, customerId, pageable);
        } else if (status != null) {
            return warrantyTicketRepository.findByStatus(status, pageable);
        } else if (customerId != null) {
            return warrantyTicketRepository.findByCustomerId(customerId, pageable);
        }
        return warrantyTicketRepository.findAll(pageable);
    }

    @Override
    public Optional<WarrantyTicket> getTicketById(Long id) {
        return warrantyTicketRepository.findById(id);
    }

    @Override
    public Optional<WarrantyTicket> updateStatus(Long id, WarrantyStatus newStatus, String notes) {
        return warrantyTicketRepository.findById(id).map(ticket -> {
            ticket.setStatus(newStatus);
            if (notes != null) {
                ticket.setTechnicalNotes(notes);
            }
            return warrantyTicketRepository.save(ticket);
        });
    }

    @Override
    public Optional<WarrantyTicket> updateTicket(Long id, WarrantyTicket ticketDetails) {
        return warrantyTicketRepository.findById(id).map(ticket -> {
            // Cập nhật các trường cần thiết
            ticket.setIssueDescription(ticketDetails.getIssueDescription());
            ticket.setRepairCost(ticketDetails.getRepairCost());
            ticket.setExpectedReturnDate(ticketDetails.getExpectedReturnDate());
            // ... các trường khác
            return warrantyTicketRepository.save(ticket);
        });
    }
}