package com.android.chen.filesecuritysystem.FileControl;

import android.os.Handler;

import com.android.chen.filesecuritysystem.Tools.MessageConstant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by leixun on 16/12/1.
 */

public class FileIO {


    File decryptionFile;
    File encryptionFile;
    private FileOutputStream outputStream;
    private BufferedOutputStream bufferedOutputStream;
    private Handler mHandler;


    public FileIO(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void createFile(String createFilePath, String deleteFilePath, byte[] bytes, String type) throws IOException {
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

    public boolean deleteFile(String filePath, String type) {
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
