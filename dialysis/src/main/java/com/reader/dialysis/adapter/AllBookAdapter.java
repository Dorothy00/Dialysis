package com.reader.dialysis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.reader.dialysis.Model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/25.
 */
public class AllBookAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private AddBookCallBack mCallBack;
    private List<Book> mBookList = new ArrayList<>();

    public interface AddBookCallBack {
        void addBook(int pos);
    }

    public AllBookAdapter(Context context) {
        mContext = context;
        mCallBack = (AddBookCallBack)context;
    }

    public void setData(List<Book> bookList) {
        if (bookList != null) {
            mBookList.clear();
            mBookList.addAll(bookList);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Book getItem(int position) {
        if (position < 0 || position >= mBookList.size()) {
            return null;
        }
        return mBookList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_all_book, null);
            holder.ivCover = (ImageView) convertView.findViewById(R.id.book_cover);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.author);
            holder.btnAdd = (Button) convertView.findViewById(R.id.collect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != null) {
            Book book = getItem(position);
            holder.tvTitle.setText(book.getTitle());
            holder.tvAuthor.setText(book.getAuthor());
            AVFile avFile = book.getCoverUrl();
            String url = avFile.getUrl();
            Picasso.with(mContext).load(url).into(holder.ivCover);
            if (book.isAdded()) {
                holder.btnAdd.setVisibility(View.INVISIBLE);
            } else {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.btnAdd.setTag(position);
                holder.btnAdd.setOnClickListener(this);
            }
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mCallBack == null || !(v.getTag() instanceof Integer)) {
            return;
        }
        int pos = (Integer) v.getTag();
        mCallBack.addBook(pos);
    }

    private class ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
        Button btnAdd;
    }
}
