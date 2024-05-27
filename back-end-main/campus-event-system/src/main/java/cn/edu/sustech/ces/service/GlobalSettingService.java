package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.GlobalSetting;
import cn.edu.sustech.ces.repository.GlobalSettingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GlobalSettingService {

    private final GlobalSettingRepository globalSettingRepository;

    public static final String COMMENT_MAX_WEIGHT = "comment_max_weight";
    public static final String COMMENT_IMAGE_WEIGHT = "comment_image_weight";
    public static final String COMMENT_VIDEO_WEIGHT = "comment_video_weight";
    public static final String COMMENT_MAX_SIZE = "comment_max_size";
    public static final String CATEGORIES = "categories";
    public static final String MAX_PAGE_SIZE = "max_page_size";
    public static final String ORDER_EXPIRE_TIME = "order_expire_time";
    public static final String AMAP_API_CODE = "amap_api_code";
    public static final String AMAP_API_KEY = "amap_api_key";
    public static final String[] DEFAULT_KEYS = {COMMENT_MAX_WEIGHT, COMMENT_IMAGE_WEIGHT, COMMENT_VIDEO_WEIGHT, CATEGORIES, ORDER_EXPIRE_TIME, AMAP_API_CODE, AMAP_API_KEY};

    @Cacheable(cacheNames = "globalSettings", key = "#key")
    public String getSetting(String key) {
        key = key.toLowerCase();
        GlobalSetting setting = globalSettingRepository.findById(key).orElse(null);
        if (setting == null) {
            String defaultValue = null;
            if (key.equals(COMMENT_MAX_WEIGHT)) {
                defaultValue = "9";
            }
            if (key.equals(COMMENT_IMAGE_WEIGHT)) {
                defaultValue = "1";
            }
            if (key.equals(COMMENT_VIDEO_WEIGHT)) {
                defaultValue = "9";
            }
            if (key.equals(COMMENT_MAX_SIZE)) {
                defaultValue = "10485760";
            }
            if (key.equals(MAX_PAGE_SIZE)) {
                defaultValue = "20";
            }
            if (key.equals(CATEGORIES)) {
                defaultValue = "course,activity,music";
            }
            if (key.equals(ORDER_EXPIRE_TIME)) {
                defaultValue = ""  + (1000 * 60 * 10);
            }
            if (key.equals(AMAP_API_CODE)) {
                defaultValue = "";
            }
            if (key.equals(AMAP_API_KEY)) {
                defaultValue = "";
            }
            if (defaultValue != null) {
                setting = new GlobalSetting(key, defaultValue);
                globalSettingRepository.save(setting);
            }
            return defaultValue;
        }
        return setting.getValue();
    }

    @CachePut(cacheNames = "globalSettings", key = "#key")
    public String setSetting(String key, String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            return value;
        }
        key = key.toLowerCase();
        globalSettingRepository.save(new GlobalSetting(key, value));
        return value;
    }


    public List<GlobalSetting> getSettings() {
        for (String key : DEFAULT_KEYS) {
            getSetting(key);
        }
        return globalSettingRepository.findAll();
    }
}