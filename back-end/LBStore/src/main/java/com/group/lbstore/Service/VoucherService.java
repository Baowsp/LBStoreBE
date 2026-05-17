package com.group.lbstore.Service;

import com.group.lbstore.Model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface VoucherService {
    Page<Voucher> getAllVouchers(Pageable pageable);
    Optional<Voucher> getVoucherById(UUID id);
    Optional<Voucher> getVoucherByCode(String code);
    Voucher createVoucher(Voucher voucher);
    Voucher updateVoucher(UUID id, Voucher voucher);
    void deleteVoucher(UUID id);
    
    // For Checkout
    Voucher validateAndApplyVoucher(String code, BigDecimal orderAmount);
}
