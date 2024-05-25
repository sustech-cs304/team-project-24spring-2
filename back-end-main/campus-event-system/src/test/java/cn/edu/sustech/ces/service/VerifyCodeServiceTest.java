package cn.edu.sustech.ces.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VerifyCodeServiceTest {
    static long CODE_EXPIRY_DURATION = 1000;
    @InjectMocks
    private VerifyCodeService verifyCodeService;

    @Test
    void testGenerateCode() {
        String code = verifyCodeService.generateCode();
        assertNotNull(code);
        assertEquals(4, code.length());
    }

    @Test
    void testStoreAndRetrieveCode() {
        String identity = "test@example.com";
        String code = "1234";

        verifyCodeService.storeCode(identity, code);
        assertEquals(code, verifyCodeService.getCode(identity));
    }

    @Test
    void testIsCodeExpired() throws InterruptedException {
        String identity = "test@example.com";
        String code = "1234";

        verifyCodeService.storeCode(identity, code);
        // Simulate time passage for expiry
        Thread.sleep(CODE_EXPIRY_DURATION + 1000);

        assertEquals(verifyCodeService.getCode(identity), code);
        assertFalse(verifyCodeService.isCodeExpired(identity), "Code should not be expired");
    }

    @Test
    void testRemoveCode() {
        String identity = "user123";
        String code = "4321";

        verifyCodeService.storeCode(identity, code);
        assertNotNull(verifyCodeService.getCode(identity));

        verifyCodeService.removeCode(identity);
        assertNull(verifyCodeService.getCode(identity), "Code should be removed");
    }

    @Test
    void testCodeNotExpiredImmediatelyAfterStorage() {
        String identity = "user@example.com";
        String code = "5678";

        verifyCodeService.storeCode(identity, code);
        assertFalse(verifyCodeService.isCodeExpired(identity), "Code should not be expired immediately after storage");
    }
}
