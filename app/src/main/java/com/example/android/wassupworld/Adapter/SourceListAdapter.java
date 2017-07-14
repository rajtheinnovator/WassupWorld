package com.example.android.wassupworld.Adapter;

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
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by dell on 7/6/2017.
 */

public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.SourceListAdapterViewHolder> {
    private Cursor mCursor;
    private final Context mContext;

    final private SourceListAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface SourceListAdapterOnClickHandler {
        void onClick(String url);
    }

    public SourceListAdapter(Context context, Cursor cursor, SourceListAdapterOnClickHandler clickHandler) {
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
    public SourceListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.later_list_item, parent, false);

        view.setFocusable(true);

        return new SourceListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SourceListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String auther = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_AUTHOR));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_TITLE));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE));
        String cat = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_CATEGORY));
        long dateInUnix = mCursor.getLong(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_DATE));

        Date date = new Date((long) dateInUnix * 1000);

        String prettyTimeString = new PrettyTime(Locale.ENGLISH).format(date);
        if (!TextUtils.isEmpty(imageUrl))
            Picasso.with(mContext).load(imageUrl).into(holder.laterImageView);

        holder.authernTextView.setText("by:" + auther);
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

    class SourceListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView laterImageView;
        final TextView descriptionTextView;
        final TextView titleTextView;
        final TextView catogeryTextView;
        final TextView dateTextView;
        final TextView authernTextView;

        final ImageButton watchLaterButton;


        SourceListAdapterViewHolder(View view) {
            super(view);

            laterImageView = (ImageView) view.findViewById(R.id.iv_later);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_descreption_later);
            titleTextView = (TextView) view.findViewById(R.id.tv_title_later);
            catogeryTextView = (TextView) view.findViewById(R.id.tv_category_later);
            dateTextView = (TextView) view.findViewById(R.id.tv_date_later);
            authernTextView = (TextView) view.findViewById(R.id.tv_auther_later);
            watchLaterButton= (ImageButton) view.findViewById(R.id.button_watch_later_later);
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

            String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_URL));
            mClickHandler.onClick(url);
        }
    }
}
