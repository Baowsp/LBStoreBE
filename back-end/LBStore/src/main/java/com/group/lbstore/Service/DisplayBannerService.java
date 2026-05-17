package com.group.lbstore.Service;
import com.group.lbstore.Model.DisplayBanner;
import java.util.List;

public interface DisplayBannerService {
    DisplayBanner createDisplayBanner(Long bannerId, DisplayBanner displayBannerDetails);
    DisplayBanner updateDisplayBanner(Long id, Long bannerId, DisplayBanner displayBannerDetails);
    void deleteDisplayBanner(Long id);
    DisplayBanner getDisplayBannerById(Long id);
    List<DisplayBanner> getAllDisplayBanners();
}
