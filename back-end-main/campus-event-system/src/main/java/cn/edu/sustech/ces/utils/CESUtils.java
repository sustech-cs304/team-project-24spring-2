package cn.edu.sustech.ces.utils;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.CESUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CESUtils {

    private CESUtils() {
    }

    public static User getAuthorizedUser() {
        Object object =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(object instanceof CESUserDetails userDetails)) {
            return null;
        }
        return userDetails.getUser();
    }

}
