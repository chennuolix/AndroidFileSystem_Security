package com.android.chen.filesecuritysystem.Bean;

/**
 * Created by leixun on 16/12/2.
 */

public class FileItem {

    public static final String TYPE_FILE_ENCRYPTED = "encrypted_file";
    public static final String TYPE_FILE_DECRYPT = "decrypt_file";
    public static final String TYPE_DIRECTORY = "directory";


    private String filePath;
    private String fileName;
    private String type;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
