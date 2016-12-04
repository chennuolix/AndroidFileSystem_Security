package com.android.chen.filesecuritysystem.DES;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by leixun on 16/12/1.
 */

public class DESDecryption {

    private byte[] mCiphertext;
    private byte[] mKey;

    private byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
        mCiphertext = ciphertext;
        mKey = key;

        SecureRandom secureRandom = new SecureRandom();

        DESKeySpec desKeySpec = new DESKeySpec(mKey);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

        return cipher.doFinal(mCiphertext);
    }

}
