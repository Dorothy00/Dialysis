package com.reader.dialysis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.reader.dialysis.Model.AVUserBook;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/8.
 */
public class UserbookAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<AVUserBook> mAVUserBookList = new ArrayList<>();
    private DeleteUserBookCallBack mCallBack;

    public interface DeleteUserBookCallBack {
        void delete(int pos);
    }

    public UserbookAdapter(Context context, DeleteUserBookCallBack callBack
    ) {
        this.mContext = context;
        mCallBack = callBack;
    }

    public void setData(List<AVUserBook> AVUserBookList) {
        if (AVUserBookList != null) {
            mAVUserBookList.clear();
            mAVUserBookList.addAll(AVUserBookList);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mAVUserBookList.size();
    }

    @Override
    public AVUserBook getItem(int position) {
        if (position < 0 || position >= mAVUserBookList.size()) {
            return null;
        }
        return mAVUserBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_userbook, null);
            holder.ivCover = (ImageView) convertView.findViewById(R.id.book_cover);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.author);
            holder.ivDelete = (ImageButton) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != null) {
            AVUserBook AVUserBook = getItem(position);
            holder.tvTitle.setText(AVUserBook.getTitle());
            holder.tvAuthor.setText(AVUserBook.getAuthor());
            AVFile avFile = AVUserBook.getCoverUrl();
            String url = avFile.getUrl();
            Picasso.with(mContext).load(url).into(holder.ivCover);
            holder.ivDelete.setTag(position);
            holder.ivDelete.setOnClickListener(this);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mCallBack == null || !(v.getTag() instanceof Integer)) {
            return;
        }

        int pos = (Integer) v.getTag();
        mCallBack.delete(pos);
    }

    private class ViewHolder {
        ImageView ivCover;
        ImageButton ivDelete;
        TextView tvTitle;
        TextView tvAuthor;
    }
}
