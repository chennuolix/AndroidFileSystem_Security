package com.android.chen.filesecuritysystem.ValidationPassword;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

;

/**
 * Created by leixun on 16/12/11.
 */

public class VerifyPasswd {

    private static int ValidationCodeLength = 16;

    private MessageDigest md;
    private Context mContext;
    private byte[] validationByte;
    private byte[] mdByte;
    private String deviceId;


    String TAG = "VerifyPasswd";

    public VerifyPasswd(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 向加密文件中写入校验码
     *
     * @param encryptByte
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] addVerifyCode(byte[] encryptByte, String key) throws NoSuchAlgorithmException {
        validationByte = Md5Passwd(key);
        byte[] allByte = new byte[encryptByte.length + validationByte.length];
        System.arraycopy(validationByte, 0, allByte, 0, validationByte.length);
        System.arraycopy(encryptByte, 0, allByte, validationByte.length, encryptByte.length);
        return allByte;
    }


    /**
     * 验证校验码是否正确
     *
     * @param allByte
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] verifyValidationcode(byte[] allByte, String key) throws NoSuchAlgorithmException {
        validationByte = new byte[ValidationCodeLength];
        for (int i = 0; i < ValidationCodeLength; i++) {
            validationByte[i] = allByte[i];
        }
        if (Arrays.equals(validationByte, Md5Passwd(key))) {
            byte[] encryptByte = new byte[allByte.length - ValidationCodeLength];
            for (int i = 0; i < encryptByte.length; i++) {
                encryptByte[i] = allByte[i + ValidationCodeLength];
            }
            return encryptByte;
        }
        Toast.makeText(mContext, "密码不正确，请重试!", Toast.LENGTH_SHORT).show();
        return null;
    }


    /**
     * 密码+设备ID组成的字符串来进行MD5
     *
     * @param passswd
     * @return
     * @throws NoSuchAlgorithmException
     */
    private byte[] Md5Passwd(String passswd) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        md.update((passswd + deviceId).getBytes());
        mdByte = md.digest();
        return mdByte;
    }

}
