package com.erasm.util;
import org.springframework.stereotype.Component; import java.util.regex.Pattern;
@Component public class ValidationUtil {
    private static final Pattern PWD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern HTML = Pattern.compile("<[^>]*>");
    public boolean isValidPassword(String p) { return p != null && PWD.matcher(p).matches(); }
    public boolean containsHtml(String s) { return s != null && HTML.matcher(s).find(); }
    public String sanitize(String s) { return s == null ? null : s.replaceAll("<[^>]*>", "").trim(); }
}
