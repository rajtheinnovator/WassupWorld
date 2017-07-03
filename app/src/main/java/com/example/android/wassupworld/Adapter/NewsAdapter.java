package com.example.android.wassupworld.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;


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

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        view.setFocusable(true);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String auther = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_AUTHOR));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE));
        String cat = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_CATEGORY));
        long date = mCursor.getLong(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DATE));
        Picasso.with(mContext).load(imageUrl).into(holder.newsImageView);
        holder.authorTextView.setText(auther);
        holder.descriptionTextView.setText(des);
        holder.titleTextView.setText(title);
        holder.dateTextView.setText(date+"");
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
        final TextView authorTextView;
        final TextView descriptionTextView;
        final TextView titleTextView;
        final TextView catogeryTextView;
        final TextView dateTextView;


        NewsAdapterViewHolder(View view) {
            super(view);

            newsImageView = (ImageView) view.findViewById(R.id.iv_news);
            authorTextView = (TextView) view.findViewById(R.id.tv_author);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_descreption);
            titleTextView = (TextView) view.findViewById(R.id.tv_title);
            catogeryTextView = (TextView) view.findViewById(R.id.tv_category);
            dateTextView = (TextView) view.findViewById(R.id.tv_date);

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
            String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
            mClickHandler.onClick(url);
        }
    }
}
