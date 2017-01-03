package com.android.chen.filesecuritysystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chen.filesecuritysystem.Bean.FileItem;
import com.android.chen.filesecuritysystem.Callback.ItemClickCallback;
<<<<<<< HEAD
import com.android.chen.filesecuritysystem.R;
import com.android.chen.filesecuritysystem.Tools.FilePathHeap;
=======
import com.android.chen.filesecuritysystem.Callback.ItemLongClickCallback;
import com.android.chen.filesecuritysystem.R;
import com.android.chen.filesecuritysystem.FileControl.FilePathHeap;
>>>>>>> dev_chen

import java.util.List;

/**
 * Created by leixun on 16/12/2.
 */

public class FileListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<FileItem> mFiles;

    private ItemClickCallback itemClickCallback;
<<<<<<< HEAD

    static final String TAG = "TAG_FileAdapter";


    public FileListAdapter(Context mContext, List<FileItem> mFiles, ItemClickCallback itemClickCallback) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.itemClickCallback = itemClickCallback;
=======
    private ItemLongClickCallback itemLongClickCallback;




    public FileListAdapter(Context mContext, List<FileItem> mFiles, ItemClickCallback itemClickCallback, ItemLongClickCallback itemLongClickCallback) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.itemClickCallback = itemClickCallback;
        this.itemLongClickCallback = itemLongClickCallback;
>>>>>>> dev_chen
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_filelist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String type = mFiles.get(position).getType();
        switch (type) {
            case FileItem.TYPE_FILE_ENCRYPTED:
                holder.ivFileIcon.setImageResource(R.mipmap.file_encrypt);
                break;
            case FileItem.TYPE_FILE_DECRYPT:
                holder.ivFileIcon.setImageResource(R.mipmap.file);
                break;
<<<<<<< HEAD
            case FileItem.TYPE_DIRECTPRY:
=======
            case FileItem.TYPE_DIRECTORY:
>>>>>>> dev_chen
                holder.ivFileIcon.setImageResource(R.mipmap.directory);
                break;
        }

        holder.tvFileName.setText(mFiles.get(position).getFileName());
        holder.itemClickCallback = itemClickCallback;
<<<<<<< HEAD
=======
        holder.itemLongClickCallback = itemLongClickCallback;
>>>>>>> dev_chen
        /**
         * 判断是否是第一个返回上一级的item，并传入对应的文件路径。
         */
        holder.filePath = mFiles.get(position).getFilePath();
        holder.type = type;
        holder.mContext = mContext;
        holder.position = position;
<<<<<<< HEAD

    }

=======
    }


>>>>>>> dev_chen
}

class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    ImageView ivFileIcon;
    TextView tvFileName;

    Context mContext;
    ItemClickCallback itemClickCallback;
<<<<<<< HEAD
=======
    ItemLongClickCallback itemLongClickCallback;
>>>>>>> dev_chen
    String filePath;
    String type;
    int position;

<<<<<<< HEAD
    static final String TAG = "TAG_MyViewHolder";

    public MyViewHolder(View itemView) {
=======

    MyViewHolder(View itemView) {
>>>>>>> dev_chen
        super(itemView);
        ivFileIcon = (ImageView) itemView.findViewById(R.id.ivFileIcon);
        tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
<<<<<<< HEAD
        if (type.equals(FileItem.TYPE_DIRECTPRY)) {
            if (position == 0) {
                filePath = FilePathHeap.pop();
                if (filePath == null){
=======
        if (type.equals(FileItem.TYPE_DIRECTORY)) {
            if (position == 0) {
                filePath = FilePathHeap.pop();
                if (filePath == null) {
>>>>>>> dev_chen
                    Toast.makeText(mContext, "已经是根目录了", Toast.LENGTH_SHORT).show();
                    return;
                }
                filePath = FilePathHeap.filePathList.get(0);
            } else {
                FilePathHeap.push(filePath);
            }
            itemClickCallback.updateView(filePath);
        } else {
<<<<<<< HEAD
            Toast.makeText(mContext, "这不是一个文件夹", Toast.LENGTH_SHORT).show();
=======
            Toast.makeText(mContext, "长按加密或解密", Toast.LENGTH_SHORT).show();
>>>>>>> dev_chen
        }
    }

    @Override
    public boolean onLongClick(View v) {
<<<<<<< HEAD
        return false;
=======
        itemLongClickCallback.longClick(filePath,type);
        return true;
>>>>>>> dev_chen
    }
}