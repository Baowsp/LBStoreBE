package com.group.lbstore.Controller;

import com.group.lbstore.Model.Banner;
import com.group.lbstore.Model.BannerPosition;
import com.group.lbstore.Service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Banner> createBanner(@ModelAttribute Banner banner) {
        Banner createdBanner = bannerService.createBanner(banner);
        return new ResponseEntity<>(createdBanner, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Banner>> getAllBanners(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BannerPosition position,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Banner> banners = bannerService.findBanners(isActive, position, pageable);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBannerById(@PathVariable Long id) {
        return bannerService.getBannerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Banner> updateBanner(@PathVariable Long id, @ModelAttribute Banner bannerDetails) {
        return bannerService.updateBanner(id, bannerDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}