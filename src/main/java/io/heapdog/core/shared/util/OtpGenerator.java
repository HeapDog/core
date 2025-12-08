package io.heapdog.core.shared.util;

import java.security.SecureRandom;

public class OtpGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateOtp() {
        int DEFAULT_OTP_LENGTH = 6;
        return generateOtp(DEFAULT_OTP_LENGTH);
    }

    public static String generateOtp(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(POOL.length());
            otp.append(POOL.charAt(index));
        }
        return otp.toString();
    }
}
