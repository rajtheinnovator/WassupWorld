package com.example.android.wassupworld.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by dell on 7/6/2017.
 */

public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.SourceListAdapterViewHolder> {
    public final static String SOURCE = "source";
    public final static String CATEGORY = "category";
    private final Context mContext;
    final private SourceListAdapterOnClickHandler mClickHandler;
    public String mType;
    private Cursor mCursor;
    private int lastPosition = -1;

    public SourceListAdapter(Context context, Cursor cursor, SourceListAdapterOnClickHandler clickHandler, String type) {
        mContext = context;
        mCursor = cursor;
        mClickHandler = clickHandler;
        mType = type;
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public SourceListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);

        view.setFocusable(true);

        return new SourceListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SourceListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String source = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE));
        String author = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_AUTHOR));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL_TO_IMAGE));
        String cat = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_CATEGORY));
        long dateInUnix = mCursor.getLong(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_DATE));
        int later = mCursor.getInt(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_LATER));
        Date date = new Date((long) dateInUnix * 1000);

        String prettyTimeString = new PrettyTime(Locale.ENGLISH).format(date);
        if (mType.equals(SOURCE)) {
            holder.sourceImageView.setVisibility(View.GONE);
            holder.author.setText(author);
            holder.catogeryTextView.setText(cat);
        } else {
            holder.catogeryTextView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(source))
                Picasso.with(mContext).load(Icons.getImageUrl(source)).into(holder.sourceImageView);
        }

        if (!TextUtils.isEmpty(imageUrl))
            Picasso.with(mContext).load(imageUrl).into(holder.newsImageView);
        if (later == 0) {
            holder.watchLaterButton.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            holder.watchLaterButton.setTag(R.drawable.ic_bookmark_border_black_24dp);
        } else {
            holder.watchLaterButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
            holder.watchLaterButton.setTag(R.drawable.ic_bookmark_black_24dp);
        }
        holder.descriptionTextView.setText(des);
        holder.titleTextView.setText(title);
        holder.dateTextView.setText(prettyTimeString);

        setAnimation(holder.itemView, position);


    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
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

    /**
     * The interface that receives onClick messages.
     */
    public interface SourceListAdapterOnClickHandler {
        void onClick(String url, int tag);
    }

    class SourceListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView newsImageView;
        final ImageView sourceImageView;
        final TextView descriptionTextView;
        final TextView titleTextView;
        final TextView catogeryTextView;
        final TextView dateTextView;
        final ImageButton watchLaterButton;
        final CardView newsCardView;
        final ImageButton shareButton;
        final TextView author;

        SourceListAdapterViewHolder(View view) {
            super(view);
            newsCardView = (CardView) view.findViewById(R.id.cv_news);
            newsImageView = (ImageView) view.findViewById(R.id.iv_news);
            sourceImageView = (ImageView) view.findViewById(R.id.iv_source_logo);
            author = (TextView) view.findViewById(R.id.author_news);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_descreption_news);
            titleTextView = (TextView) view.findViewById(R.id.tv_title_news);
            catogeryTextView = (TextView) view.findViewById(R.id.tv_category_news);
            dateTextView = (TextView) view.findViewById(R.id.tv_date_news);
            watchLaterButton = (ImageButton) view.findViewById(R.id.button_watch_later_news);
            watchLaterButton.setOnClickListener(this);
            shareButton = (ImageButton) view.findViewById(R.id.button_share_news);
            shareButton.setOnClickListener(this);

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

            int id = mCursor.getInt(mCursor.getColumnIndex(NewsContract.NewsnEntry._ID));

            if (v.getId() == R.id.button_watch_later_news) {
                Integer resource = (Integer) watchLaterButton.getTag();

                if (resource == R.drawable.ic_bookmark_black_24dp) {


                    String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_TITLE));
                    int deleted = mContext.getContentResolver().delete(NewsContract.WatchLaterEntry.CONTENT_URI,
                            NewsContract.WatchLaterEntry.COLUMN_TITLE + " = ?",
                            new String[]{title + ""});

                    Toast.makeText(mContext, mContext.getString(R.string.deleted_from_read_later), Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsnEntry.COLUMN_LATER, 0);
                    int updated = mContext.getContentResolver().update(NewsContract.NewsnEntry.CONTENT_URI,
                            contentValues, NewsContract.NewsnEntry._ID + " = ?",
                            new String[]{id + ""});
                    //  notifyItemRemoved(adapterPosition);
                } else {

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
                    watchLaterButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    watchLaterButton.setTag(R.drawable.ic_bookmark_black_24dp);
                    Toast.makeText(mContext, mContext.getString(R.string.added_to_read_later), Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsnEntry.COLUMN_LATER, 1);
                    int updated = mContext.getContentResolver().update(NewsContract.NewsnEntry.CONTENT_URI,
                            contentValues, NewsContract.NewsnEntry._ID + " = ?",
                            new String[]{id + ""});
                    //   notifyItemInserted(adapterPosition);
                }
            } else if (v.getId() == R.id.cv_news) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
                mClickHandler.onClick(url, 0);


            } else if (v.getId() == R.id.button_share_news) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
                mClickHandler.onClick(url, 1);

            }
        }

    }
}
