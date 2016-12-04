package com.android.chen.filesecuritysystem;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.chen.filesecuritysystem.Adapter.FileListAdapter;
import com.android.chen.filesecuritysystem.Bean.FileItem;
import com.android.chen.filesecuritysystem.Callback.ItemClickCallback;
import com.android.chen.filesecuritysystem.Tools.FilePathHeap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    static final String ROOT_PATH = "/";

    CollapsingToolbarLayout mCollapsingToolbarLayout;
    List<FileItem> fileItems = new ArrayList<>();

    FileListAdapter mAdapter;

    RecyclerView rvFileList;

    LinearLayoutManager linearLayoutManager;

    ItemClickCallback itemClickCallback = new ItemClickCallback() {
        @Override
        public void updateView(String path) {
            showFileDir(path);
        }
    };

    static final String TAG = "TAG_MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FilePathHeap.push(ROOT_PATH);
        showFileDir(ROOT_PATH);
    }

    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout.setTitle("文件加解密系统");
        mCollapsingToolbarLayout.setExpandedTitleColor(R.color.white);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(R.color.black);
        rvFileList = (RecyclerView) findViewById(R.id.rvFileList);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvFileList.setLayoutManager(linearLayoutManager);
        rvFileList.setNestedScrollingEnabled(false);
    }

    private void showFileDir(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        FileItem fileItem;
        String fileName;
        String filePath;
        File typeFile;
        fileItems = new ArrayList<>();
        /**
         * 增加第一个返回上个路径的item
         */
        addFirstItem();
        if (files != null) {
            for (File file1 : files) {
                fileItem = new FileItem();
                fileName = file1.getName();
                filePath = file1.getAbsolutePath();
                fileItem.setFileName(fileName);
                fileItem.setFilePath(filePath);
                typeFile = new File(filePath);
                if (!typeFile.isDirectory()) {
                    fileItem.setType(FileItem.TYPE_FILE_DECRYPT);
                    if (fileName.length() > 7) {
                        if (fileName.substring(fileName.length() - 7).equalsIgnoreCase(".cipher")) {
                            fileItem.setType(FileItem.TYPE_FILE_ENCRYPTED);
                        }
                    }
                } else {
                    fileItem.setType(FileItem.TYPE_DIRECTPRY);
                }
                fileItems.add(fileItem);
            }
        }
        mAdapter = new FileListAdapter(MainActivity.this, fileItems, itemClickCallback);
        rvFileList.setAdapter(mAdapter);
    }


    private void addFirstItem() {
        FileItem fileItem = new FileItem();
        fileItem.setType(FileItem.TYPE_DIRECTPRY);
        fileItem.setFileName("..");
        fileItems.add(fileItem);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
