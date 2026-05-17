package com.group.lbstore.Controller;

import com.group.lbstore.Model.DisplayBanner;
import com.group.lbstore.Service.DisplayBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/display-banners")
@CrossOrigin(origins = "http://localhost:5173")
public class DisplayBannerController {

    @Autowired
    private DisplayBannerService displayBannerService;

    @PostMapping
    public ResponseEntity<DisplayBanner> createDisplayBanner(
            @RequestParam Long bannerId,
            @RequestBody DisplayBanner displayBanner) {
        DisplayBanner created = displayBannerService.createDisplayBanner(bannerId, displayBanner);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayBanner> updateDisplayBanner(
            @PathVariable Long id,
            @RequestParam(required = false) Long bannerId,
            @RequestBody DisplayBanner displayBanner) {
        DisplayBanner updated = displayBannerService.updateDisplayBanner(id, bannerId, displayBanner);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisplayBanner(@PathVariable Long id) {
        displayBannerService.deleteDisplayBanner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayBanner> getDisplayBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(displayBannerService.getDisplayBannerById(id));
    }

    @GetMapping
    public ResponseEntity<List<DisplayBanner>> getAllDisplayBanners() {
        return ResponseEntity.ok(displayBannerService.getAllDisplayBanners());
    }
}
