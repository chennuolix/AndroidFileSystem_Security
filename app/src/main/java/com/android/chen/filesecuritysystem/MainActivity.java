package com.android.chen.filesecuritysystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.chen.filesecuritysystem.Adapter.FileListAdapter;
import com.android.chen.filesecuritysystem.Bean.FileItem;
import com.android.chen.filesecuritysystem.Callback.ItemClickCallback;
import com.android.chen.filesecuritysystem.Callback.ItemLongClickCallback;
import com.android.chen.filesecuritysystem.CryptionFile.CryptionFile;
import com.android.chen.filesecuritysystem.FileControl.FilePathHeap;
import com.android.chen.filesecuritysystem.Tools.MessageConstant;

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

    AlertDialog.Builder alertDialogBuilder;
    ProgressBar progressBar;

    View view_alertDialog;
    EditText etPasswd;
    EditText etConfirmPasswd;
    LinearLayout llConfirmView;

    CryptionFile cryptionFile;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageConstant.MSG_ENCRYPT_SUCCESSFUL:
                    Toast.makeText(MainActivity.this, "加密成功", Toast.LENGTH_SHORT).show();
                    showFileDir(FilePathHeap.filePathList.get(0));
                    break;
                case MessageConstant.MSG_DECRYPT_SUCCESSFUL:
                    Toast.makeText(MainActivity.this, "解密成功", Toast.LENGTH_SHORT).show();
                    showFileDir(FilePathHeap.filePathList.get(0));
                    break;
            }
        }
    };


    ItemClickCallback itemClickCallback = new ItemClickCallback() {
        @Override
        public void updateView(String path) {
            showFileDir(path);
        }
    };

    ItemLongClickCallback itemLongClickCallback = new ItemLongClickCallback() {
        @Override
        public void longClick(final String filePath, String type) {
            switch (type) {
                case FileItem.TYPE_DIRECTORY:
                    alertDialogBuilder.setTitle("sorry");
                    alertDialogBuilder.setMessage("对不起，暂不支持文件夹加密");
                    alertDialogBuilder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                    break;
                case FileItem.TYPE_FILE_DECRYPT:
                    /**
                     * 长按未加密文件
                     */
                    view_alertDialog = getLayoutInflater().inflate(R.layout.view_alertdialog, null);
                    etPasswd = (EditText) view_alertDialog.findViewById(R.id.etPasswd);
                    etConfirmPasswd = (EditText) view_alertDialog.findViewById(R.id.etConfirmPasswd);
                    alertDialogBuilder.setTitle("加密提示");
                    alertDialogBuilder.setView(view_alertDialog);
                    alertDialogBuilder.setPositiveButton("加密", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String passwd = etPasswd.getText().toString();
                            String confirmPasswd = etConfirmPasswd.getText().toString();
                            if (!passwd.equals("") && !confirmPasswd.equals("")) {
                                if (passwd.length() >= 8) {
                                    if (passwd.equals(confirmPasswd)) {
                                        dialog.dismiss();
                                        /**
                                         * 加密文件
                                         */
                                        try {
                                            EncryptFile(passwd, filePath, mHandler);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "请输入密码，并确认密码!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "请输入至少8位密码!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                    break;
                case FileItem.TYPE_FILE_ENCRYPTED:
                    /**
                     * 长按解密文件
                     */
                    view_alertDialog = getLayoutInflater().inflate(R.layout.view_alertdialog, null);
                    etPasswd = (EditText) view_alertDialog.findViewById(R.id.etPasswd);
                    etConfirmPasswd = (EditText) view_alertDialog.findViewById(R.id.etConfirmPasswd);
                    llConfirmView = (LinearLayout) view_alertDialog.findViewById(R.id.confirmView);
                    llConfirmView.setVisibility(View.GONE);
                    alertDialogBuilder.setTitle("解密提示");
                    alertDialogBuilder.setView(view_alertDialog);
                    alertDialogBuilder.setPositiveButton("解密", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String passwd = etPasswd.getText().toString();
                            if (!passwd.equals("")) {
                                /**
                                 * 解密文件
                                 */
                                try {
                                    DecryptFile(passwd, filePath, mHandler);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                    break;
            }
        }
    };

    static final String TAG = "Security";

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
        rvFileList.setItemAnimator(new DefaultItemAnimator());
        alertDialogBuilder = new AlertDialog.Builder(this);
        view_alertDialog = getLayoutInflater().inflate(R.layout.view_alertdialog, null);
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
                    fileItem.setType(FileItem.TYPE_DIRECTORY);
                }
                fileItems.add(fileItem);
            }
        }
        mAdapter = new FileListAdapter(MainActivity.this, fileItems, itemClickCallback, itemLongClickCallback);
        rvFileList.setAdapter(mAdapter);
    }


    private void addFirstItem() {
        FileItem fileItem = new FileItem();
        fileItem.setType(FileItem.TYPE_DIRECTORY);
        fileItem.setFileName("..");
        fileItems.add(fileItem);
    }

    private void EncryptFile(String key, String filePath, Handler mHandler) throws Exception {
        cryptionFile = new CryptionFile(key, mHandler, MainActivity.this);
        cryptionFile.encryptFile(filePath);
    }

    private void DecryptFile(String key, String filePath, Handler mHandler) throws Exception {
        cryptionFile = new CryptionFile(key, mHandler, MainActivity.this);
        cryptionFile.decryptFile(filePath);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
