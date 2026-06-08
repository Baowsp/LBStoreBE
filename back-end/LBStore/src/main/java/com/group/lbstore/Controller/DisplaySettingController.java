package com.group.lbstore.Controller;

import com.group.lbstore.Model.DisplaySetting;
import com.group.lbstore.Repository.DisplaySettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings/display")
@RequiredArgsConstructor
public class DisplaySettingController {

    private final DisplaySettingRepository repo;

    /** Các key hợp lệ và giá trị mặc định */
    private static final Map<String, Integer> DEFAULTS = new HashMap<>();
    static {
        DEFAULTS.put("home_cols",          4);
        DEFAULTS.put("home_rows",          2);
        DEFAULTS.put("search_cols",        5);
        DEFAULTS.put("search_rows",        4);
        DEFAULTS.put("promo_cols",         5);
        DEFAULTS.put("promo_rows",         2);
        DEFAULTS.put("promoted_page_cols", 5);
        DEFAULTS.put("promoted_page_rows", 4);
    }

    /**
     * GET /api/settings/display
     * Trả về tất cả settings (kết hợp DB + default nếu chưa có).
     */
    @GetMapping
    public ResponseEntity<Map<String, Integer>> getAll() {
        Map<String, Integer> result = new HashMap<>(DEFAULTS);
        repo.findAll().forEach(s -> result.put(s.getSettingKey(), s.getSettingValue()));
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/settings/display
     * Body: { "home_cols": 4, "search_rows": 3, ... }
     * Lưu/cập nhật từng key. Bỏ qua các key không hợp lệ.
     */
    @PutMapping
    public ResponseEntity<Map<String, Integer>> saveAll(@RequestBody Map<String, Integer> body) {
        for (Map.Entry<String, Integer> entry : body.entrySet()) {
            String key = entry.getKey();
            if (!DEFAULTS.containsKey(key)) continue; // bỏ qua key lạ
            DisplaySetting setting = repo.findById(key)
                    .orElse(DisplaySetting.builder()
                            .settingKey(key)
                            .description(key.replace('_', ' '))
                            .build());
            setting.setSettingValue(entry.getValue());
            repo.save(setting);
        }
        return getAll();
    }
}
