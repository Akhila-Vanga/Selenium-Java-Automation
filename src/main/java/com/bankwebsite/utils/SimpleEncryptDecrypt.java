package com.bankwebsite.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SimpleEncryptDecrypt {

    private static final String ALGO = "AES";

    // Generate a new AES key and return it as Base64 string
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGO);
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Convert Base64 encoded key string back to SecretKey
    public static SecretKey getKeyFromBase64String(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGO);
    }

    // Encrypt plain text using the provided SecretKey
    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decrypt encrypted text using the provided SecretKey
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    public static void main(String[] args) throws Exception {
        String secretKey = generateKey();
        System.out.println("Secret Key (save this safely): " + secretKey);

        String plainPassword = "Tester1";  // Replace this with  actual DB password
        SecretKey key = getKeyFromBase64String(secretKey);
        String encryptedPassword = encrypt(plainPassword, key);
        System.out.println("Encrypted Password (put this in config.properties): " + encryptedPassword);

        String decryptedPassword = decrypt(encryptedPassword, key);
        System.out.println("Decrypted Password (for verification): " + decryptedPassword);
    }
}
