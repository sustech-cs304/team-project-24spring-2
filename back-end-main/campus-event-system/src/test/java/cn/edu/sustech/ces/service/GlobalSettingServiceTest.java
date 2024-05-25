package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.GlobalSetting;
import cn.edu.sustech.ces.repository.GlobalSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GlobalSettingServiceTest {

    @Mock
    private GlobalSettingRepository globalSettingRepository;
    @InjectMocks
    private GlobalSettingService globalSettingService;


    @Test
    void testGetSettingWithDefaultValue() {
        when(globalSettingRepository.findById(anyString())).thenReturn(Optional.empty());

        String COMMENT_MAX_WEIGHT = globalSettingService.getSetting(GlobalSettingService.COMMENT_MAX_WEIGHT);
        assertEquals("9", COMMENT_MAX_WEIGHT);
        verify(globalSettingRepository).save(new GlobalSetting(GlobalSettingService.COMMENT_MAX_WEIGHT, "9"));

        String COMMENT_IMAGE_WEIGHT = globalSettingService.getSetting(GlobalSettingService.COMMENT_IMAGE_WEIGHT);
        assertEquals("1", COMMENT_IMAGE_WEIGHT);
        verify(globalSettingRepository).save(new GlobalSetting(GlobalSettingService.COMMENT_IMAGE_WEIGHT, "1"));

        String COMMENT_MAX_SIZE = globalSettingService.getSetting(GlobalSettingService.COMMENT_MAX_SIZE);
        assertEquals("10485760", COMMENT_MAX_SIZE);
        verify(globalSettingRepository).save(new GlobalSetting(GlobalSettingService.COMMENT_MAX_SIZE, "10485760"));

    }

    @Test
    void testGetSettingExistingValue() {
        GlobalSetting existingSetting = new GlobalSetting(GlobalSettingService.COMMENT_MAX_WEIGHT, "10");
        when(globalSettingRepository.findById(GlobalSettingService.COMMENT_MAX_WEIGHT)).thenReturn(Optional.of(existingSetting));

        String fetchedValue = globalSettingService.getSetting(GlobalSettingService.COMMENT_MAX_WEIGHT);
        assertEquals("10", fetchedValue);
    }

    @Test
    void testSetSetting() {
        String newValue = "15";
        globalSettingService.setSetting(GlobalSettingService.COMMENT_MAX_SIZE, newValue);
        verify(globalSettingRepository).save(new GlobalSetting(GlobalSettingService.COMMENT_MAX_SIZE, newValue));
    }

    @Test
    void testGetSettings() {
        when(globalSettingRepository.findById(anyString())).thenReturn(Optional.empty());
        when(globalSettingRepository.findAll()).thenReturn(Arrays.asList(
                new GlobalSetting(GlobalSettingService.COMMENT_MAX_WEIGHT, "9"),
                new GlobalSetting(GlobalSettingService.COMMENT_IMAGE_WEIGHT, "1")
        ));

        List<GlobalSetting> settings = globalSettingService.getSettings();
        assertNotNull(settings);
        assertTrue(settings.size() >= 2);
        verify(globalSettingRepository, times(GlobalSettingService.DEFAULT_KEYS.length)).save(any(GlobalSetting.class));
    }
}
