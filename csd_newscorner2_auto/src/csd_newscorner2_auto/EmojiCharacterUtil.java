package csd_newscorner2_auto;

public class EmojiCharacterUtil {

    private static boolean isEmojiCharacter(int codePoint) {
        return (codePoint >= 9728 && codePoint <= 10175) ||
                codePoint == 12349 || codePoint == 8265 || codePoint == 8252 ||
                (codePoint >= 8192 && codePoint <= 8207) || (codePoint >= 8232 && codePoint <= 8239) ||
                codePoint == 8287 || (codePoint >= 8293 && codePoint <= 8303) ||
                (codePoint >= 8448 && codePoint <= 8527) || (codePoint >= 8960 && codePoint <= 9215) ||
                (codePoint >= 11008 && codePoint <= 11263) || (codePoint >= 10496 && codePoint <= 10623) ||
                (codePoint >= 12800 && codePoint <= 13055) || (codePoint >= 55296 && codePoint <= 57343) ||
                (codePoint >= 57344 && codePoint <= 63743) || (codePoint >= 65024 && codePoint <= 65039) ||
                codePoint >= 65536;
    }

    public static String escape(String src) {
        if (src == null) return null;

        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < src.length()) {
            int codePoint = src.codePointAt(index);
            if (isEmojiCharacter(codePoint)) {
                sb.append('&')
                        .append(Integer.toHexString(codePoint).length())
                        .append('u')
                        .append(':')
                        .append(Integer.toHexString(codePoint));
            } else {
                sb.appendCodePoint(codePoint);
            }
            index += Character.charCount(codePoint);
        }
        return sb.toString();
    }

    public static String reverse(String src) {
        if (src == null) return null;

        StringBuilder sb = new StringBuilder();
        int length = src.length();
        for (int i = 0; i < length;) {
            if (src.charAt(i) == '&' && i + 6 < length) {
                int len = Character.getNumericValue(src.charAt(i + 1));
                if (src.charAt(i + 2) == 'u' && src.charAt(i + 3) == ':') {
                    String hex = src.substring(i + 4, i + 4 + len);
                    try {
                        int codePoint = Integer.parseInt(hex, 16);
                        sb.appendCodePoint(codePoint);
                        i += 4 + len;
                        continue;
                    } catch (NumberFormatException e) {
                        // Fall back to appending the raw characters
                    }
                }
            }
            sb.append(src.charAt(i++));
        }
        return sb.toString();
    }

    public static String filter(String src) {
        if (src == null) return null;

        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < src.length()) {
            int codePoint = src.codePointAt(index);
            if (!isEmojiCharacter(codePoint)) {
                sb.appendCodePoint(codePoint);
            }
            index += Character.charCount(codePoint);
        }
        return sb.toString();
    }
}
