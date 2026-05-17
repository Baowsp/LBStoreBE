package com.group.lbstore.Service.Impl;

import com.group.lbstore.Model.Banner;
import com.group.lbstore.Model.BannerPosition;
import com.group.lbstore.Repository.BannerRepository;
import com.group.lbstore.Service.BannerService;
import com.group.lbstore.Service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Banner createBanner(Banner banner) {
        if (banner.getImageFile() != null && !banner.getImageFile().isEmpty()) {
            String fileUrl = fileStorageService.storeFile(banner.getImageFile());
            banner.setImageUrl("http://localhost:8080" + fileUrl);
        }
        return bannerRepository.save(banner);
    }

    @Override
    public Page<Banner> findBanners(Boolean isActive, BannerPosition position, Pageable pageable) {
        if (isActive != null && position != null) {
            return bannerRepository.findByIsActiveAndPosition(isActive, position, pageable);
        } else if (isActive != null) {
            return bannerRepository.findByIsActive(isActive, pageable);
        } else if (position != null) {
            return bannerRepository.findByPosition(position, pageable);
        }
        return bannerRepository.findAll(pageable);
    }

    @Override
    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    @Override
    public Optional<Banner> updateBanner(Long id, Banner bannerDetails) {
        return bannerRepository.findById(id).map(banner -> {
            if (bannerDetails.getTitle() != null) banner.setTitle(bannerDetails.getTitle());
            if (bannerDetails.getTargetUrl() != null) banner.setTargetUrl(bannerDetails.getTargetUrl());
            if (bannerDetails.getPosition() != null) banner.setPosition(bannerDetails.getPosition());
            if (bannerDetails.getCategorySlug() != null) banner.setCategorySlug(bannerDetails.getCategorySlug());
            
            // Note: Since active is a primitive boolean, we just copy it
            banner.setActive(bannerDetails.isActive());
            
            if (bannerDetails.getImageFile() != null && !bannerDetails.getImageFile().isEmpty()) {
                String fileUrl = fileStorageService.storeFile(bannerDetails.getImageFile());
                banner.setImageUrl("http://localhost:8080" + fileUrl);
            } else if (bannerDetails.getImageUrl() != null && !bannerDetails.getImageUrl().isEmpty()) {
                banner.setImageUrl(bannerDetails.getImageUrl());
            }

            return bannerRepository.save(banner);
        });
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}