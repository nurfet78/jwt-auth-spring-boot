package org.nurfet.jwtserverspring.utils;


import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(generateKey());
        System.out.println();
        System.out.println(generateKey());
    }
    private static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        keyGen.init(512, SecureRandom.getInstanceStrong());
        return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());

    }
}
