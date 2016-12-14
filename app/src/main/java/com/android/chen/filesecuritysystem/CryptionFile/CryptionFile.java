package com.android.chen.filesecuritysystem.CryptionFile;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.chen.filesecuritysystem.DES.DESDecryption;
import com.android.chen.filesecuritysystem.DES.DesEncryption;
import com.android.chen.filesecuritysystem.FileControl.FileIO;
import com.android.chen.filesecuritysystem.ValidationPassword.VerifyPasswd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

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
    private byte bytes[];
    private byte encryptByte[];
    private byte decryptByte[];
    private byte allByte[];
    private Handler mHandler;
    private Context mContext;

    private FileIO fileIO;
    private VerifyPasswd verifyPasswd;

    private String TAG = "Security";

    public CryptionFile(String key, Handler handler, Context context) {
        this.mHandler = handler;
        this.key = key.getBytes();
        this.mContext = context;

    }

    /**
     * 加密文件
     * @param filePath
     * @throws Exception
     */
    public void encryptFile(String filePath) throws Exception {
        fileIO = new FileIO(mHandler);
        verifyPasswd = new VerifyPasswd(mContext);
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
            allByte = verifyPasswd.addVerifyCode(encryptByte, new String(key));
            if (allByte != null && allByte.length > 0) {
                fileIO.createFile(filePath + ".cipher", filePath, allByte, "encrypt");
            }
        } else {
            Toast.makeText(mContext, "该文件为空!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 解密文件
     * @param filePath
     * @throws Exception
     */
    public void decryptFile(String filePath) throws Exception {
        fileIO = new FileIO(mHandler);
        verifyPasswd = new VerifyPasswd(mContext);
        encryptionFile = new File(filePath);
        inputStream = new FileInputStream(encryptionFile);
        bufferedInputStream = new BufferedInputStream(inputStream);
        if (encryptionFile.length() != 0) {
            bytes = new byte[(int) encryptionFile.length()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) bufferedInputStream.read();
            }
            bufferedInputStream.close();
            encryptByte = verifyPasswd.verifyValidationcode(bytes, new String(key));
            if (encryptByte == null) {
                return;
            }
            decryptByte = desDecryption.decrypt(encryptByte, key);
            if (decryptByte != null && decryptByte.length > 0) {
                String createFilePath = filePath.substring(0, filePath.length() - 7);
                fileIO.createFile(createFilePath, filePath, decryptByte, "decrypt");
            }
        } else {
            Toast.makeText(mContext, "该文件为空!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}

