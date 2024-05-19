package cn.edu.sustech.ces.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerifyCodeService {

    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> timeStorage = new ConcurrentHashMap<>();
    private static final long CODE_EXPIRY_DURATION = 5 * 60 * 1000;

    public void storeCode(String identity, String code) {
        codeStorage.put(identity, code);
        timeStorage.put(identity, System.currentTimeMillis());
    }

    public String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
    }

    public String getCode(String identity) {
        if (isCodeExpired(identity)) {
            removeCode(identity);
            return null;
        }
        return codeStorage.get(identity);
    }

    public Long getCodeTime(String identity) {
        return timeStorage.get(identity);
    }

    public boolean isCodeExpired(String identity) {
        Long storageTime = timeStorage.get(identity);
        return storageTime == null || System.currentTimeMillis() > storageTime + CODE_EXPIRY_DURATION;
    }

    public void removeCode(String identity) {
        codeStorage.remove(identity);
        timeStorage.remove(identity);
    }

}
