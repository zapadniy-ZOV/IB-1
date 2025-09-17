package itmo.labs.secure_api.util;

import org.owasp.encoder.Encode;

public final class Sanitizer {
    private Sanitizer() {
    }

    public static String forHtml(String input) {
        if (input == null)
            return null;
        return Encode.forHtml(input);
    }
}
