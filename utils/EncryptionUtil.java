package utils;

import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {
    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final int PASSWORD_LENGTH = 12;

    public static String encryptPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }
        return encryptPassword(password).equals(storedPassword);
    }

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        String allChars = CHAR_LOWERCASE + CHAR_UPPERCASE + DIGIT + SPECIAL_CHARS;

        password.append(CHAR_LOWERCASE.charAt(random.nextInt(CHAR_LOWERCASE.length())));
        password.append(CHAR_UPPERCASE.charAt(random.nextInt(CHAR_UPPERCASE.length())));
        password.append(DIGIT.charAt(random.nextInt(DIGIT.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        char[] passwordChars = password.toString().toCharArray();
        for (int i = passwordChars.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = passwordChars[index];
            passwordChars[index] = passwordChars[i];
            passwordChars[i] = temp;
        }

        return new String(passwordChars);
    }
}
