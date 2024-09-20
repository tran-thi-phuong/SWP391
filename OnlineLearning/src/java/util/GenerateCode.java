package util;

import java.security.SecureRandom;

public class GenerateCode {
    public static final int CODE_LENGTH = 6;

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int digit = random.nextInt(10); 
            sb.append(digit);
        }

        return sb.toString();
    }
    public static void main(String[] args){
        
        System.out.println(GenerateCode.generateRandomCode());
    }
}