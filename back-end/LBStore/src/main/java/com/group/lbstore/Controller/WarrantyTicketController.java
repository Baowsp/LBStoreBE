package com.group.lbstore.Controller;

import com.group.lbstore.Model.WarrantyStatus;
import com.group.lbstore.Model.WarrantyTicket;
import com.group.lbstore.Service.WarrantyTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/warranty-tickets")
@RequiredArgsConstructor
public class WarrantyTicketController {

    private final WarrantyTicketService warrantyTicketService;

    /**
     * LƯU Ý: DTO ở đây là rất quan trọng. Một CreateWarrantyTicketRequestDTO nên được dùng
     * để nhận các ID cho productItem, customer, và technicalEmployee, cùng với mô tả lỗi.
     * Một WarrantyTicketResponseDTO sẽ được dùng để trả về thông tin chi tiết.
     */

    @PostMapping
    public ResponseEntity<WarrantyTicket> createWarrantyTicket(@RequestBody WarrantyTicket ticketRequest) {
        // Request body nên chứa ID của các entity liên quan.
        // VD: { "productItemId": 1, "customerId": 2, "technicalEmployeeId": 3, "issueDescription": "..." }
        // Service sẽ lấy các entity này và tạo phiếu bảo hành.
        WarrantyTicket createdTicket = warrantyTicketService.createTicket(ticketRequest);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<WarrantyTicket>> getAllWarrantyTickets(
            @RequestParam(required = false) WarrantyStatus status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WarrantyTicket> tickets = warrantyTicketService.findTickets(status, customerId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarrantyTicket> getWarrantyTicketById(@PathVariable Long id) {
        return warrantyTicketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WarrantyTicket> updateWarrantyStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        // Ví dụ request body: { "status": "IN_PROGRESS", "notes": "Đã kiểm tra, xác nhận lỗi IC nguồn." }
        WarrantyStatus newStatus = WarrantyStatus.valueOf(statusUpdate.get("status"));
        String notes = statusUpdate.get("notes");
        return warrantyTicketService.updateStatus(id, newStatus, notes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarrantyTicket> updateWarrantyTicket(@PathVariable Long id, @RequestBody WarrantyTicket ticketDetails) {
        return warrantyTicketService.updateTicket(id, ticketDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}