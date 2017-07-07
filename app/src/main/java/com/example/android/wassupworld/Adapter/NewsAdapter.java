package com.example.android.wassupworld.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;


/**
 * Created by dell on 7/2/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private Cursor mCursor;
    private final Context mContext;

    final private AdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterOnClickHandler {
        void onClick(String url);
    }

    public NewsAdapter(Context context, Cursor cursor, AdapterOnClickHandler clickHandler) {
        mContext = context;
        mCursor = cursor;

        this.mClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);

        view.setFocusable(true);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String source = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE));
        String cat = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_CATEGORY));
        long dateInUnix = mCursor.getLong(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DATE));

        Date date = new Date((long) dateInUnix * 1000);

        String prettyTimeString = new PrettyTime(Locale.ENGLISH).format(date);
        if (!TextUtils.isEmpty(imageUrl))
            Picasso.with(mContext).load(imageUrl).into(holder.newsImageView);
        if (!TextUtils.isEmpty(source))
            Picasso.with(mContext).load(Icons.getImageUrl(source)).into(holder.sourceImageView);


        holder.descriptionTextView.setText(des);
        holder.titleTextView.setText(title);
        holder.dateTextView.setText(prettyTimeString);
        holder.catogeryTextView.setText(cat);


    }

    /**
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position. This method is useful when we want to use different layouts for different items
     * depending on their position. In Sunshine, we take advantage of this method to provide a
     * different layout for the "today" layout. The "today" layout is only shown in portrait mode
     * with the first item in the list.
     *
     * @param position index within our RecyclerView and Cursor
     * @return the view type (today or future day)
     */
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * Swaps the cursor used by the ForecastAdapter for its weather data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the weather data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView newsImageView;
        final ImageView sourceImageView;
        final TextView descriptionTextView;
        final TextView titleTextView;
        final TextView catogeryTextView;
        final TextView dateTextView;
        final ImageButton watchLaterButton;

        NewsAdapterViewHolder(View view) {
            super(view);

            newsImageView = (ImageView) view.findViewById(R.id.iv_news);
            sourceImageView = (ImageView) view.findViewById(R.id.iv_source_logo);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_descreption_news);
            titleTextView = (TextView) view.findViewById(R.id.tv_title_news);
            catogeryTextView = (TextView) view.findViewById(R.id.tv_category_news);
            dateTextView = (TextView) view.findViewById(R.id.tv_date_news);
            watchLaterButton = (ImageButton) view.findViewById(R.id.button_watch_later_news);
            watchLaterButton.setOnClickListener(this);
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
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);


            if (v.getId() == R.id.button_watch_later_news) {
                 ContentValues itemValues = new ContentValues();

                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_CATEGORY, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_AUTHOR, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_AUTHOR)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DATE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DATE)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DESCRIPTION)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_TITLE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE)));
                itemValues.put(NewsContract.WatchLaterEntry.COLUMN_SOURCE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE)));


                mContext.getContentResolver().insert(NewsContract.WatchLaterEntry.CONTENT_URI, itemValues);

            }
            else if(v.getId() == R.id.button_watch_later_news) {
            String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
            mClickHandler.onClick(url);
        }

        }
    }

}
