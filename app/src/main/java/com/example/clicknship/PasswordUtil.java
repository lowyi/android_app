package com.example.clicknship;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class PasswordUtil {

    public static byte[] encrypt(String keyStr, byte[] bytes) throws Exception{

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("XgY5ThsGKpjSiFCCI08mMELdeFyNPYAhK1qX/MfUenuzoaS7geTJWkp1GsVCY5El".getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return encrypt(ivBytes, keyBytes, bytes);
    }

    static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception{
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    public static byte[] decrypt(String keyStr, byte[] bytes) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("XgY5ThsGKpjSiFCCI08mMELdeFyNPYAhK1qX/MfUenuzoaS7geTJWkp1GsVCY5El".getBytes());
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes());
        byte[] keyBytes = sha.digest();

        return decrypt(ivBytes, keyBytes, bytes);
    }

    static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes)  throws Exception{
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    public static String encryptStrAndToBase64(String keyStr, String enStr) throws Exception{
        byte[] bytes = encrypt(keyStr, enStr.getBytes("UTF-8"));
        return new String(Base64.encode(bytes ,Base64.NO_WRAP), "UTF-8");
    }

    public static String decryptStrAndFromBase64(String keyStr, String deStr) throws Exception{
        byte[] bytes = decrypt(keyStr, Base64.decode(deStr.getBytes("UTF-8"),Base64.NO_WRAP));
        return new String(bytes, "UTF-8");
    }

}
