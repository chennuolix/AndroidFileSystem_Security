package com.android.chen.filesecuritysystem.CryptionFile;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.chen.filesecuritysystem.DES.DESDecryption;
import com.android.chen.filesecuritysystem.DES.DesEncryption;
import com.android.chen.filesecuritysystem.Tools.MessageConstant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by leixun on 16/12/6.
 */

public class CryptionFile {

    private byte[] key;

    private DesEncryption desEncryption = new DesEncryption();
    private DESDecryption desDecryption = new DESDecryption();
    File decryptionFile;
    File encryptionFile;
    private FileInputStream inputStream;
    private BufferedInputStream bufferedInputStream;
    private FileOutputStream outputStream;
    private BufferedOutputStream bufferedOutputStream;
    private byte bytes[];
    private byte encryptByte[];
    private byte decryptByte[];
    private Handler mHandler;
    private Context mContext;

    private String TAG = "Security";

    public CryptionFile(String key, Handler handler, Context context) {
        this.mHandler = handler;
        this.key = key.getBytes();
        this.mContext = context;
    }

    public void encryptFile(String filePath) throws Exception {
        decryptionFile = new File(filePath);
        inputStream = new FileInputStream(decryptionFile);
        bufferedInputStream = new BufferedInputStream(inputStream);
        if (decryptionFile.length() != 0) {
            bytes = new byte[(int) decryptionFile.length()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) bufferedInputStream.read();
            }
            bufferedInputStream.close();
            encryptByte = desEncryption.encrypt(bytes, key);
            Log.d(TAG, "encryptFile: encrypt file" + new String(encryptByte));
            if (encryptByte != null && encryptByte.length > 0) {
                createFile(filePath + ".cipher", filePath, encryptByte, "encrypt");
            }
        } else {
            Toast.makeText(mContext, "该文件为空!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void decryptFile(String filePath) throws Exception {
        Log.d(TAG, "decryptFile: start");
        encryptionFile = new File(filePath);
        inputStream = new FileInputStream(encryptionFile);
        bufferedInputStream = new BufferedInputStream(inputStream);
        if (encryptionFile.length() != 0) {
            bytes = new byte[(int) encryptionFile.length()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) bufferedInputStream.read();
            }
            bufferedInputStream.close();
            decryptByte = desDecryption.decrypt(bytes, key);
            Log.d(TAG, "decryptFile: end" + new String(decryptByte));
            if (decryptByte != null && decryptByte.length > 0) {
                String createFilePath = filePath.substring(0, filePath.length() - 7);
                createFile(createFilePath, filePath, decryptByte, "decrypt");
            }
        } else {
            Toast.makeText(mContext, "该文件为空!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void createFile(String createFilePath, String deleteFilePath, byte[] bytes, String type) throws IOException {
        encryptionFile = new File(createFilePath);
        outputStream = new FileOutputStream(encryptionFile);
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        if (encryptionFile.exists()) {
            for (int i = 0; i < bytes.length; i++) {
                bufferedOutputStream.write(bytes[i]);
            }
            bufferedOutputStream.close();
            deleteFile(deleteFilePath, type);
        } else {
            mHandler.obtainMessage(MessageConstant.MSG_ERROR);
        }
    }

    private boolean deleteFile(String filePath, String type) {
        boolean isSuccessful = false;
        decryptionFile = new File(filePath);
        if (decryptionFile.isFile() && decryptionFile.isAbsolute()) {
            isSuccessful = decryptionFile.delete();
            if (isSuccessful) {
                if (type.equalsIgnoreCase("encrypt")) {
                    mHandler.obtainMessage(MessageConstant.MSG_ENCRYPT_SUCCESSFUL).sendToTarget();
                } else {
                    mHandler.obtainMessage(MessageConstant.MSG_DECRYPT_SUCCESSFUL).sendToTarget();
                }
            }
        }
        return isSuccessful;
    }

}

