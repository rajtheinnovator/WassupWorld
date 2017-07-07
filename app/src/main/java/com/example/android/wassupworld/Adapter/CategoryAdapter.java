package com.example.android.wassupworld.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;

import java.util.ArrayList;

/**
 * Created by dell on 7/7/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder> {


    private final Context mContext;

    final private CatAdapterOnClickHandler mClickHandler;

    ArrayList<String> mArray;

    /**
     * The interface that receives onClick messages.
     */
    public interface CatAdapterOnClickHandler {
        void onClick(String url);
    }

    public CategoryAdapter(Context context, CatAdapterOnClickHandler clickHandler, ArrayList<String> array) {
        mContext = context;
        mArray = array;
        this.mClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

    @Override
    public CategoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.category_list_item, parent, false);

        view.setFocusable(true);

        return new CategoryAdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(CategoryAdapterViewHolder holder, int position) {

        String title = mArray.get(position);
        holder.titleTextView.setText(title.toUpperCase());
        int x=Icons.getCategroyColor(title);
        holder.backgroundImageView.setImageResource(Icons.getCategroyIcon(title));
        holder.layout.setBackgroundColor(mContext.getResources().getColor(x,null));


    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class CategoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView backgroundImageView;
        final TextView titleTextView;
        final LinearLayout layout;

        CategoryAdapterViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.ll_category);
            backgroundImageView = (ImageView) view.findViewById(R.id.iv_category_iconn);
            titleTextView = (TextView) view.findViewById(R.id.tv_category_title);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
//            int adapterPosition = getAdapterPosition();
//            mCursor.moveToPosition(adapterPosition);
//
//
//            if (v.getId() == R.id.button_watch_later_news) {
//                ContentValues itemValues = new ContentValues();
//
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_CATEGORY, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_AUTHOR, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_AUTHOR)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DATE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DATE)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DESCRIPTION)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_TITLE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE)));
//                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_SOURCE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE)));
//
//
//                mContext.getContentResolver().insert(NewsContract.WatchLaterEntry.CONTENT_URI, itemValues);
//
//            }
//            else if(v.getId() == R.id.button_watch_later_news) {
//                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
//                mClickHandler.onClick(url);
//            }

        }
    }
}
