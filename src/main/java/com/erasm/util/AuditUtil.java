package com.erasm.util;
import org.springframework.security.core.Authentication; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component public class AuditUtil {
    public String getCurrentUserEmail() { Authentication a = SecurityContextHolder.getContext().getAuthentication(); return (a != null && a.isAuthenticated() && !"anonymousUser".equals(a.getPrincipal())) ? a.getName() : "SYSTEM"; }
}
