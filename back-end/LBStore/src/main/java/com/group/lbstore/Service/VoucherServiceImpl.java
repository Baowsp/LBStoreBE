package com.group.lbstore.Service;

import com.group.lbstore.Model.Voucher;
import com.group.lbstore.Repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public Page<Voucher> getAllVouchers(Pageable pageable) {
        return voucherRepository.findAllVouchers(pageable);
    }

    @Override
    public Optional<Voucher> getVoucherById(UUID id) {
        return voucherRepository.findById(id);
    }

    @Override
    public Optional<Voucher> getVoucherByCode(String code) {
        if (code == null) return Optional.empty();
        return voucherRepository.findByCode(code.toUpperCase().trim());
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        if (voucher.getCode() != null) {
            Optional<Voucher> existing = voucherRepository.findByCode(voucher.getCode().toUpperCase().trim());
            if (existing.isPresent()) {
                throw new RuntimeException("Mã voucher đã tồn tại");
            }
        }
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(UUID id, Voucher voucherDetails) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));

        voucher.setDiscountPercentage(voucherDetails.getDiscountPercentage());
        voucher.setMaxDiscountAmount(voucherDetails.getMaxDiscountAmount());
        voucher.setMinOrderValue(voucherDetails.getMinOrderValue());
        voucher.setStartDate(voucherDetails.getStartDate());
        voucher.setEndDate(voucherDetails.getEndDate());
        voucher.setUsageLimit(voucherDetails.getUsageLimit());
        voucher.setIsActive(voucherDetails.getIsActive());
        voucher.setDescription(voucherDetails.getDescription());

        return voucherRepository.save(voucher);
    }

    @Override
    public void deleteVoucher(UUID id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Voucher validateAndApplyVoucher(String code, BigDecimal orderAmount) {
        if (code == null || code.trim().isEmpty()) {
            throw new RuntimeException("Mã voucher không được để trống");
        }

        Voucher voucher = voucherRepository.findByCode(code.toUpperCase().trim())
                .orElseThrow(() -> new RuntimeException("Mã voucher không tồn tại"));

        if (!voucher.getIsActive()) {
            throw new RuntimeException("Mã voucher đã bị vô hiệu hóa");
        }

        LocalDateTime now = LocalDateTime.now();
        if (voucher.getStartDate() != null && now.isBefore(voucher.getStartDate())) {
            throw new RuntimeException("Mã voucher chưa bắt đầu có hiệu lực");
        }
        if (voucher.getEndDate() != null && now.isAfter(voucher.getEndDate())) {
            throw new RuntimeException("Mã voucher đã hết hạn");
        }

        if (voucher.getUsageLimit() != null && voucher.getUsageLimit() > 0) {
            int currentUsed = voucher.getUsedCount() != null ? voucher.getUsedCount() : 0;
            if (currentUsed >= voucher.getUsageLimit()) {
                throw new RuntimeException("Mã voucher đã hết lượt sử dụng");
            }
        }

        if (voucher.getMinOrderValue() != null && orderAmount.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu " + voucher.getMinOrderValue() + " để áp dụng voucher");
        }

        return voucher;
    }
}
