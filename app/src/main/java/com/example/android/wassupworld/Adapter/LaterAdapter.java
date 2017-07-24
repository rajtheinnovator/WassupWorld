package com.example.android.wassupworld.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
 * Created by dell on 7/14/2017.
 */

public class LaterAdapter extends RecyclerView.Adapter<LaterAdapter.LaterAdapterViewHolder> {
    private final Context mContext;
    final private LaterAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private int lastPosition = -1;

    public LaterAdapter(Context context, Cursor cursor, LaterAdapterOnClickHandler clickHandler) {
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
    public LaterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);

        view.setFocusable(true);

        return new LaterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LaterAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String source = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_SOURCE));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_TITLE));
        String des = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_DESCRIPTION));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_URL_TO_IMAGE));
        String cat = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_CATEGORY));
        long dateInUnix = mCursor.getLong(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_DATE));
         Date date = new Date((long) dateInUnix * 1000);

        String prettyTimeString = new PrettyTime(Locale.ENGLISH).format(date);
        if (!TextUtils.isEmpty(imageUrl))
            Picasso.with(mContext).load(imageUrl).into(holder.newsImageView);
        if (!TextUtils.isEmpty(source))
            Picasso.with(mContext).load(Icons.getImageUrl(source)).into(holder.sourceImageView);


            holder.watchLaterButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
            holder.watchLaterButton.setTag(R.drawable.ic_bookmark_black_24dp);

        holder.descriptionTextView.setText(des);
        holder.titleTextView.setText(title);
        holder.dateTextView.setText(prettyTimeString);
        holder.catogeryTextView.setText(cat);
        setAnimation(holder.itemView, position);


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

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(LaterAdapterViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((LaterAdapterViewHolder)holder).itemView.clearAnimation();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface LaterAdapterOnClickHandler {
        void onClick(String url, int tag);
    }

    class LaterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView newsImageView;
        final ImageView sourceImageView;
        final TextView descriptionTextView;
        final TextView titleTextView;
        final TextView catogeryTextView;
        final TextView dateTextView;
        final ImageButton watchLaterButton;
        final ImageButton shareButton;

        LaterAdapterViewHolder(View view) {
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
            shareButton = (ImageButton) view.findViewById(R.id.button_share_news);
            shareButton.setOnClickListener(this);
            newsImageView.setOnClickListener(this);
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
                Integer resource = (Integer) watchLaterButton.getTag();


                    int id = mCursor.getInt(mCursor.getColumnIndex(NewsContract.WatchLaterEntry._ID));

                    String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.WatchLaterEntry.COLUMN_TITLE));
                    int deleted = mContext.getContentResolver().delete(NewsContract.WatchLaterEntry.CONTENT_URI,
                            NewsContract.WatchLaterEntry._ID + " = ?",
                            new String[]{id + ""});

                    Toast.makeText(mContext, mContext.getString(R.string.deleted_from_read_later), Toast.LENGTH_LONG).show();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(NewsContract.NewsnEntry.COLUMN_LATER, 0);
                    int updated = mContext.getContentResolver().update(NewsContract.NewsnEntry.CONTENT_URI,
                            contentValues,   NewsContract.NewsnEntry.COLUMN_TITLE + " = ?",
                            new String[]{title + ""});
                notifyItemRemoved(adapterPosition);




            } else if (v.getId() == R.id.cv_news) {
               String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
               mClickHandler.onClick(url,0);


            }
            else if (v.getId() == R.id.button_share_news) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_URL));
                mClickHandler.onClick(url,1);

            } else if (v.getId() == R.id.iv_source_logo) {
                String url = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsnEntry.COLUMN_SOURCE));
                mClickHandler.onClick(url, 2);
            }
       }
  }}

