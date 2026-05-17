package com.group.lbstore.Repository;

import com.group.lbstore.Model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    Optional<Voucher> findByCode(String code);
    
    @Query("SELECT v FROM Voucher v ORDER BY v.createdAt DESC")
    Page<Voucher> findAllVouchers(Pageable pageable);
}
