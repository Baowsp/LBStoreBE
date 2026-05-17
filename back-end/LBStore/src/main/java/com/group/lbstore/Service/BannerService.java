package com.group.lbstore.Service;

import com.group.lbstore.Model.Banner;
import com.group.lbstore.Model.BannerPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BannerService {
    Banner createBanner(Banner banner);

    Page<Banner> findBanners(Boolean isActive, BannerPosition position, Pageable pageable);

    Optional<Banner> getBannerById(Long id);

    Optional<Banner> updateBanner(Long id, Banner bannerDetails);

    void deleteBanner(Long id);
}