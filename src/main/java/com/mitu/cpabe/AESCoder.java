package com.mitu.cpabe;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESCoder {

    /**
     * Gets the raw key.
     *
     * @param seed the seed
     * @return the raw key
     */
    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    /**
     * Encrypt.
     *
     * @param seed      the seed
     * @param plaintext the plaintext
     * @return the byte[]
     */
    public static byte[] encrypt(byte[] seed, byte[] plaintext)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
        byte[] raw = getRawKey(seed);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        System.out.println(" IN AES CODE: Encrypted successfully!");
        return cipher.doFinal(plaintext);
    }

    /**
     * Decrypt.
     *
     * @param seed       the seed
     * @param ciphertext the ciphertext
     * @return the byte[]
     */
    public static byte[] decrypt(byte[] seed, byte[] ciphertext)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] raw = getRawKey(seed);
        System.out.println("IN AES CODE: DECRYPT");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        return cipher.doFinal(ciphertext);
    }

}
