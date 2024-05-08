package cn.edu.sustech.ces.utils;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.CESUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CESUtils {

    private CESUtils() {
    }

    public static User getAuthorizedUser() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

}
