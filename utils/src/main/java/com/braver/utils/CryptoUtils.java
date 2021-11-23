/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils;

import android.util.Base64;
import android.util.Log;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static final String ECDH_ALGORITHM = "ECDH";
    public static final String SECP521R1_STD_NAME = "secp521r1";

    /**
     * Method Used to Generate Public and Private Keys using ECDH Algorithm
     */
    public static String[] generateKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECDH_ALGORITHM, new BouncyCastleProvider());
            keyPairGenerator.initialize(new ECGenParameterSpec(SECP521R1_STD_NAME), new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String privateKeyHex = encodedPrivateKeyToHex(keyPair.getPrivate());
            String publicKeyHex = encodedPublicKeyToHex(keyPair.getPublic());
            return new String[]{publicKeyHex, privateKeyHex};
        } catch (Exception e) {
            Log.d("##generateKeyPair", "-------->" + e.getMessage());
            return new String[]{"", ""};
        }
    }

    /**
     * @param key - Contains PrivateKey
     * @return - PrivateKey as HexString
     * Method used to Convert key to HEX
     */
    private static String encodedPrivateKeyToHex(PrivateKey key) {
        String encodedHex = "";
        try {
            ECPrivateKey eckey = (ECPrivateKey) key;
            encodedHex = bytesToHex(eckey.getD().toByteArray()).toLowerCase();
        } catch (Exception e) {
            Log.d("##generateKeyPair", "encodedPrivateKeyToHex-------->" + e.getMessage());
        }
        return encodedHex;
    }

    /**
     * @param key - Contains Public
     * @return - PrivateKey as HexString
     * Method used to Convert key to HEX
     */
    private static String encodedPublicKeyToHex(PublicKey key) {
        String encodedHex = "";
        try {
            ECPublicKey eckey = (ECPublicKey) key;
            encodedHex = bytesToHex(eckey.getQ().getEncoded()).toLowerCase();
        } catch (Exception e) {
            Log.d("##generateKeyPair", "encodedPublicKeyToHex-------->" + e.getMessage());
        }
        return encodedHex;
    }


    /**
     * @param dataPrv - contains PrivateKey as Hex
     * @param dataPub - contains publicKey as Hex
     * @return - SharedKey as HEX
     * Method used to Generate Shared SecretKey using PrivateKey and PublicKey
     */
    public static String getSecretKey(String dataPrv, String dataPub) {
        String secretKey = "";
        try {
            byte[] apiPrivData = HexToBytes(dataPrv);
            byte[] apiPubData = HexToBytes(dataPub);
            KeyAgreement ka = KeyAgreement.getInstance(ECDH_ALGORITHM, new BouncyCastleProvider());
            ka.init(dataToPrivateKey(apiPrivData));
            ka.doPhase(dataToPublicKey(apiPubData), true);
            byte[] secret = ka.generateSecret();
            secretKey = bytesToHex(secret).toLowerCase();
        } catch (Exception e) {
            Log.d("##getSecretKey", "-------->" + e.getMessage());
        }
        return secretKey;
    }

    /**
     * @param data - Contains PublicKey as byte[]
     * @return -  byte[] as PublicKey format
     * Method used to convert byte[] to Key for Generate SharedKey
     */
    private static PublicKey dataToPublicKey(byte[] data) {
        PublicKey publicKey = null;
        try {
            ECParameterSpec params = ECNamedCurveTable.getParameterSpec(SECP521R1_STD_NAME);
            ECPublicKeySpec pubKey = new ECPublicKeySpec(params.getCurve().decodePoint(data), params);
            KeyFactory kf = KeyFactory.getInstance(ECDH_ALGORITHM, new BouncyCastleProvider());
            publicKey = kf.generatePublic(pubKey);
        } catch (Exception e) {
            Log.d("##dataToPublicKey", "-------->" + e.getMessage());
        }
        return publicKey;
    }

    /**
     * @param data - Contains PrivateKey as byte[]
     * @return -  byte[] as PrivateKey format
     * Method used to convert byte[] to Key for Generate SharedKey
     */
    private static PrivateKey dataToPrivateKey(byte[] data) {
        PrivateKey privateKey = null;
        try {
            ECParameterSpec params = ECNamedCurveTable.getParameterSpec(SECP521R1_STD_NAME);
            ECPrivateKeySpec prvkey = new ECPrivateKeySpec(new BigInteger(data), params);
            KeyFactory kf = KeyFactory.getInstance(ECDH_ALGORITHM, new BouncyCastleProvider());
            privateKey = kf.generatePrivate(prvkey);
        } catch (Exception e) {
            Log.d("##dataToPrivateKey", "-------->" + e.getMessage());
        }
        return privateKey;
    }


    /**
     * @param secretKey  - String data
     * @param normalText -  normal text
     * @return - Encrypted text
     * Method Used to Encryption
     */
    public static String callEncryptMethod(String secretKey, String normalText) {
        String encryptedStringData = "";
        try {
            //Generate Random IV - Hex format(32 char)
            String IV = getRandomHexString().toLowerCase();
            //byte[] ivBytes = new byte[16];
            byte[] ivBytes = HexToBytes(IV);
            //Initiate Cipher
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            //Convert String to byte[]
            byte[] keyData = normalText.getBytes();
            byte[] passwordData = secretKey.getBytes(StandardCharsets.UTF_8);
            //Convert Base64 to SHA-256 Hash-key in byte[]
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = digest.digest(passwordData);
            //Convert HashBytes to Base64
            String passHashBase64 = Base64.encodeToString(passwordHash, Base64.DEFAULT);
            // Checking Base64 length
            passHashBase64 = passHashBase64.length() > 32 ? passHashBase64.substring(0, 32) : passHashBase64;
            // Convert base64 in Data to SecretKey formatted key
            Key keySpec = new SecretKeySpec(passHashBase64.getBytes(), "AES");
            //Initiate Encrypt Mode
            //Initiate IVParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            //Encrypted Data in byte[]
            byte[] encryptedMessage = cipher.doFinal(keyData);
            //byte[] to Hex String
            String encryptedHex = bytesToHex(encryptedMessage).toLowerCase();
            String finalText = IV.concat(encryptedHex);
            byte[] finalByteArray = HexToBytes(finalText);
            encryptedStringData = Base64.encodeToString(finalByteArray, Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("##callEncryptMethod", "---------->" + e.getMessage());
            return encryptedStringData;
        }
        return encryptedStringData;
    }


    /**
     * @param secretKey     - String data
     * @param encryptedText - String data
     * @return - Decrypted Text
     * Method Used to Decryption
     */
    public static String callDecryptMethod(String secretKey, String encryptedText) {
        String decryptedString = "";
        try {
            byte[] finalByteArray = Base64.decode(encryptedText, Base64.DEFAULT);
            String finalText = bytesToHex(finalByteArray);
            //Split IV and Epk
            //String[] iVPrivateKeys = encryptedText.split(":");
            String IV = finalText.substring(0, 32);
            String encryptedMessage = finalText.substring(32);
            // Hex IV to byte[]
            byte[] ivBytes = HexToBytes(IV);
            //Initiate Cipher fro Decryption
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            //Convert Base64 to SHA-256 Hash_key in byte[]
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            //Convert HashBytes to Base64
            String passHashBase64 = Base64.encodeToString(passwordHash, Base64.DEFAULT);
            // Checking Base64 length
            passHashBase64 = passHashBase64.length() > 32 ? passHashBase64.substring(0, 32) : passHashBase64;
            Key keySpec = new SecretKeySpec(passHashBase64.getBytes(), "AES");
            //Initiate Decrypt Mode
            //Initiate IVParameterSpec
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            //Decrypted Data in String
            byte[] ecBytes = HexToBytes(encryptedMessage);
            decryptedString = new String(cipher.doFinal(ecBytes));
        } catch (Exception e) {
            Log.d("##callDecryptMethod", "---------->" + e.getMessage());
            return decryptedString;
        }
        return decryptedString;
    }

    /**
     * @return - Random Hex(32 bit) String
     * Method used to Generate random Hex String for Encryption(IV Generation)
     */
    private static String getRandomHexString() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 32) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.substring(0, 32);
    }

    /**
     * @param hex - Contains Hex String
     * @return - Sutiable byte[] for given HEX
     * Method used to Covert Hex to byte[]
     */
    private static byte[] HexToBytes(String hex) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }

    /**
     * @param bytes - contains byte[]
     * @return - Sutiable Hex for given byte[]
     * Method used to Convert byte[] to HEX
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
