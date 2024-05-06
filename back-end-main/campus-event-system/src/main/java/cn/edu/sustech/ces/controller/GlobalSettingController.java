package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.service.GlobalSettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/global")
@AllArgsConstructor
public class GlobalSettingController {

    private final GlobalSettingService globalSettingService;

    @PostMapping("/get-setting")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> getSetting(String key) {
        String setting = globalSettingService.getSetting(key);
        if (setting == null) {
            return ResponseEntity.badRequest().body("Setting not found");
        }
        return ResponseEntity.ok(setting);
    }

    @PostMapping("/set-setting")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> setSetting(String key, String value) {
        return ResponseEntity.ok(globalSettingService.setSetting(key, value));
    }

    @PostMapping("/get-settings")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok(globalSettingService.getSettings());
    }

}
