package com.reader.dialysis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class UserbookAdapter extends BaseAdapter {

    private Context mContext;
    private List<AVUserBook> mAVUserBookList = new ArrayList<AVUserBook>();

    public UserbookAdapter(Context context) {
        this.mContext = context;
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
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
    }
}
