package com.group.lbstore.Repository;

import com.group.lbstore.Model.Banner;
import com.group.lbstore.Model.BannerPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
    Page<Banner> findByIsActive(Boolean isActive, Pageable pageable);
    
    Page<Banner> findByPosition(BannerPosition position, Pageable pageable);
    
    Page<Banner> findByIsActiveAndPosition(Boolean isActive, BannerPosition position, Pageable pageable);
}