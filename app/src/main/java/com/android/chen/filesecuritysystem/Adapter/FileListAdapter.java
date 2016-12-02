package com.android.chen.filesecuritysystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chen.filesecuritysystem.Bean.FileItem;
import com.android.chen.filesecuritysystem.R;

import java.util.List;

/**
 * Created by leixun on 16/12/2.
 */

public class FileListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<FileItem> mFiles;

    static final String TAG = "TAG_FileAdapter";


    public FileListAdapter(Context mContext, List<FileItem> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
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

        switch (mFiles.get(position).getType()) {
            case FileItem.TYPE_FILE_ENCRYPTED:
                holder.ivFileIcon.setImageResource(R.mipmap.file_encrypt);
                break;
            case FileItem.TYPE_FILE_DECRYPT:
                holder.ivFileIcon.setImageResource(R.mipmap.file);
                break;
            case FileItem.TYPE_DIRECTPRY:
                holder.ivFileIcon.setImageResource(R.mipmap.directory);
                break;
        }

        holder.tvFileName.setText(mFiles.get(position).getFileName());
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView ivFileIcon;
    TextView tvFileName;

    public MyViewHolder(View itemView) {
        super(itemView);
        ivFileIcon = (ImageView) itemView.findViewById(R.id.ivFileIcon);
        tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
    }
}