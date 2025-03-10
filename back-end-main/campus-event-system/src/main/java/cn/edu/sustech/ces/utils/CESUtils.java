package cn.edu.sustech.ces.utils;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.CESUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.List;

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

    public static <T> List<T> getPage(Pageable pageable, List<T> list) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return list.subList(start, end);
    }


    public static String formatTime(long expireTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(expireTime);
    }
}
