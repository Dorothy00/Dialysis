package com.reader.dialysis.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reader.dialysis.Model.Content;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/12.
 */
public class ContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Content> mContentList = new ArrayList<>();
    private int mSelectedChapterId;

    public ContentAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Content> contentList, int selectedChapterId) {
        mContentList.clear();
        if (contentList != null) {
            mContentList.addAll(contentList);
            this.mSelectedChapterId = selectedChapterId;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContentList.size();
    }

    @Override
    public Content getItem(int position) {
        if (position < 0 || position >= mContentList.size()) {
            return null;
        }
        return mContentList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_content, null);
            holder.tvContent = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Content content = getItem(position);
        if (content != null) {
            holder.tvContent.setText(content.getTitle());
            if (content.getChapterId() == mSelectedChapterId) {
                holder.tvContent.setTextColor(Resources.getSystem().getColor(android.R.color
                        .holo_blue_light));
            } else {
                holder.tvContent.setTextColor(Color.BLACK);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
    }
}
