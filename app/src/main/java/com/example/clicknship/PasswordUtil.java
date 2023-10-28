package com.example.clicknship;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class PasswordUtil {

    static byte[] encrypt(byte[] keyBytes, byte[] bytes) throws Exception{
        //AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        //cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        cipher.init(Cipher.ENCRYPT_MODE, newKey);
        return cipher.doFinal(bytes);
    }

    public static String encryptStrAndToBase64(byte[] keyStr, byte[] enStr) throws Exception{
        byte[] bytes = encrypt(keyStr, enStr);

        String base64P = Base64.encodeToString(bytes, Base64.NO_WRAP);

        return base64P;
    }

}
