package com.example.dell.smartpos;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DesEncrypter {
    Cipher ecipher;

    Cipher dcipher;

    public DesEncrypter(String key) throws Exception {
        String string = "wrmUaL+Fvz0=";
        byte[] encodedKey = Base64.decode(string);
        SecretKey seckey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
//	   SecretKey seckey = generateKey(key.toCharArray());
//		ecipher = Cipher.getInstance("AES");
//	    dcipher = Cipher.getInstance("AES");
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, seckey);
        dcipher.init(Cipher.DECRYPT_MODE, seckey);
    }


    public String encrypt(String str) throws Exception {
        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = ecipher.doFinal(utf8);
        String encStr = Base64.encodeBytes(enc);
        return encStr;
    }

    public String decrypt(String str) throws Exception {
        byte[] dec = Base64.decode(str);
        byte[] utf8 = dcipher.doFinal(dec);
        return new String(utf8, "UTF8");
    }

//	  public static SecretKey generateKey(char[] passphrase) throws NoSuchAlgorithmException, InvalidKeySpecException{
////			final int iterations = 1000;
//			final int iterations = 500;
//		    final int outputKeyLength = 192;//64 for des 128 for aes 192 for 3des
//			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//			byte[] salt = {
//				    (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
//				    (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
//				};
//			KeySpec keySpec = new PBEKeySpec(passphrase, salt, iterations, outputKeyLength);
//			SecretKey tmp = secretKeyFactory.generateSecret(keySpec);
//			SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "DESede");
//			return secretKey;
//		}
}
