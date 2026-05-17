package com.group.lbstore.Repository;
import com.group.lbstore.Model.DisplayBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayBannerRepository extends JpaRepository<DisplayBanner, Long> {
}
