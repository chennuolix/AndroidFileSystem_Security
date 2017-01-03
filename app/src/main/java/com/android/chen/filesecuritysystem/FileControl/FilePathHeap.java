package com.android.chen.filesecuritysystem.FileControl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by leixun on 16/12/4.
 */

public class FilePathHeap {

    /**
     * 利用List实现堆效果，将数据插入在第一个，删除数据时从第一个开始删除。
     * LinkedList适合用于插入删除操作多的情景。
     */

    public static List<String> filePathList = new LinkedList<>();

    public static List<String> push(String path) {
        if (filePathList != null && !"".equals(path)) {
            filePathList.add(0, path);
        }
        return filePathList;
    }

    public static String pop() {
        if (filePathList != null && filePathList.size() > 1) {
            return filePathList.remove(0);
        } else {
            return null;
        }
    }
}
