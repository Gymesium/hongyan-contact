package cn.gymesium.directory.common;

/**
 * XSS 防护：所有用户可自由输入的文本字段入库前做 HTML 转义。
 * 前端统一用插值渲染，绝不使用 v-html，形成双重防护。
 */
public final class SanitizeUtil {

    private SanitizeUtil() {
    }

    public static String clean(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
