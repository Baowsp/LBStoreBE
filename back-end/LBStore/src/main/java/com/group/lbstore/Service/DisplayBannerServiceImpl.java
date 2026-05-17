package com.group.lbstore.Service;

import com.group.lbstore.Model.Banner;
import com.group.lbstore.Model.DisplayBanner;
import com.group.lbstore.Repository.BannerRepository;
import com.group.lbstore.Repository.DisplayBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisplayBannerServiceImpl implements DisplayBannerService {

    @Autowired
    private DisplayBannerRepository displayBannerRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public DisplayBanner createDisplayBanner(Long bannerId, DisplayBanner displayBannerDetails) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + bannerId));
        displayBannerDetails.setBanner(banner);
        return displayBannerRepository.save(displayBannerDetails);
    }

    @Override
    public DisplayBanner updateDisplayBanner(Long id, Long bannerId, DisplayBanner displayBannerDetails) {
        DisplayBanner displayBanner = displayBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DisplayBanner not found with id: " + id));
        
        if (bannerId != null) {
            Banner banner = bannerRepository.findById(bannerId)
                    .orElseThrow(() -> new RuntimeException("Banner not found with id: " + bannerId));
            displayBanner.setBanner(banner);
        }

        if (displayBannerDetails.getPosition() != null) displayBanner.setPosition(displayBannerDetails.getPosition());
        if (displayBannerDetails.getCategorySlug() != null) displayBanner.setCategorySlug(displayBannerDetails.getCategorySlug());
        if (displayBannerDetails.getDisplayOrder() != null) displayBanner.setDisplayOrder(displayBannerDetails.getDisplayOrder());
        
        displayBanner.setActive(displayBannerDetails.isActive());

        return displayBannerRepository.save(displayBanner);
    }

    @Override
    public void deleteDisplayBanner(Long id) {
        DisplayBanner displayBanner = displayBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DisplayBanner not found with id: " + id));
        displayBannerRepository.delete(displayBanner);
    }

    @Override
    public DisplayBanner getDisplayBannerById(Long id) {
        return displayBannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DisplayBanner not found with id: " + id));
    }

    @Override
    public List<DisplayBanner> getAllDisplayBanners() {
        return displayBannerRepository.findAll();
    }
}
