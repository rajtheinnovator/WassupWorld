package com.ayat.android.wassupworld.adapter;

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

import com.ayat.android.wassupworld.R;
import com.ayat.android.wassupworld.provider.NewsContract;
import com.ayat.android.wassupworld.utils.Icons;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by dell on 7/6/2017.
 */

public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.SourceListAdapterViewHolder> {

    private final static String SOURCE = "source";
    private Context mContext;
    private SourceListAdapterOnClickHandler mClickHandler;
    private String mType;
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
        String source = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        String author = "By: " + mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE));
        String catNotCap = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_CATEGORY));
        String cat = catNotCap.substring(0, 1).toUpperCase() + catNotCap.substring(1);
        long dateInUnix = mCursor.getLong(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DATE));
        int later = mCursor.getInt(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_LATER));
        Date date = new Date(dateInUnix * 1000);

        String prettyTimeString = new PrettyTime(Locale.ENGLISH).format(date);
        if (mType.equals(SOURCE)) {
            holder.sourceImageView.setVisibility(View.GONE);
            holder.authorTextView.setText(author);
            holder.categoryTextView.setText(cat);
        } else {
            holder.categoryTextView.setVisibility(View.GONE);
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


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

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
        final TextView categoryTextView;
        final TextView dateTextView;
        final ImageButton watchLaterButton;
        final CardView newsCardView;
        final ImageButton shareImageButton;
        final TextView authorTextView;

        SourceListAdapterViewHolder(View view) {
            super(view);
            newsCardView = (CardView) view.findViewById(R.id.cv_news);
            newsImageView = (ImageView) view.findViewById(R.id.iv_news);
            sourceImageView = (ImageView) view.findViewById(R.id.iv_source_logo);
            authorTextView = (TextView) view.findViewById(R.id.author_news);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_description_news);
            titleTextView = (TextView) view.findViewById(R.id.tv_title_news);
            categoryTextView = (TextView) view.findViewById(R.id.tv_category_news);
            dateTextView = (TextView) view.findViewById(R.id.tv_date_news);
            watchLaterButton = (ImageButton) view.findViewById(R.id.button_watch_later_news);
            watchLaterButton.setOnClickListener(this);
            shareImageButton = (ImageButton) view.findViewById(R.id.button_share_news);
            shareImageButton.setOnClickListener(this);
            sourceImageView.setOnClickListener(this);

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

            int id = mCursor.getInt(mCursor.getColumnIndex(NewsContract.NewsEntry._ID));

            if (v.getId() == R.id.button_watch_later_news) {
                Integer resource = (Integer) watchLaterButton.getTag();

                if (resource == R.drawable.ic_bookmark_black_24dp) {


                    String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
                    mContext.getContentResolver().delete(NewsContract.WatchLaterEntry.CONTENT_URI,
                            NewsContract.WatchLaterEntry.COLUMN_TITLE + " = ?",
                            new String[]{title + ""});

                    Toast.makeText(mContext, mContext.getString(R.string.deleted_from_read_later), Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsEntry.COLUMN_LATER, 0);
                    mContext.getContentResolver().update(NewsContract.NewsEntry.CONTENT_URI,
                            contentValues, NewsContract.NewsEntry._ID + " = ?",
                            new String[]{id + ""});

                } else {

                    ContentValues itemValues = new ContentValues();
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_CATEGORY, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_AUTHOR, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DATE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DATE)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_TITLE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE)));
                    itemValues.put(NewsContract.WatchLaterEntry.COLUMN_SOURCE, mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE)));


                    mContext.getContentResolver().insert(NewsContract.WatchLaterEntry.CONTENT_URI, itemValues);
                    watchLaterButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    watchLaterButton.setTag(R.drawable.ic_bookmark_black_24dp);
                    Toast.makeText(mContext, mContext.getString(R.string.added_to_read_later), Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsEntry.COLUMN_LATER, 1);
                    mContext.getContentResolver().update(NewsContract.NewsEntry.CONTENT_URI,
                            contentValues, NewsContract.NewsEntry._ID + " = ?",
                            new String[]{id + ""});

                }
            } else if (v.getId() == R.id.cv_news) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL));
                mClickHandler.onClick(url, 0);


            } else if (v.getId() == R.id.button_share_news) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL));
                mClickHandler.onClick(url, 1);

            } else if (v.getId() == R.id.iv_source_logo) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE));
                mClickHandler.onClick(url, 2);
            }
        }

    }
}
