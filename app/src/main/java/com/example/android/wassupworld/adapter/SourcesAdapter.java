package com.example.android.wassupworld.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.provider.NewsContract;
import com.example.android.wassupworld.utils.Icons;
import com.squareup.picasso.Picasso;


public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesAdapterViewHolder> {


    private Context mContext;
    private AdapterOnClickHandlerSources mClickHandler;
    private Cursor mCursor;
    private int lastPosition = -1;

    public SourcesAdapter(Context context, Cursor cursor, AdapterOnClickHandlerSources clickHandler) {

        mContext = context;
        mCursor = cursor;
        mClickHandler = clickHandler;

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
    public void onViewDetachedFromWindow(SourcesAdapterViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        (holder).itemView.clearAnimation();
    }

    @Override
    public SourcesAdapter.SourcesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.sources_list_item, parent, false);

        view.setFocusable(true);

        return new SourcesAdapterViewHolder(view);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(SourcesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_NAME));
        String logoUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_URL_TO_IMAGE));
        String id = mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_ID));
        if (!TextUtils.isEmpty(logoUrl))
            Picasso.with(mContext).load(logoUrl).into(holder.sourcesImageView);
        else
            Picasso.with(mContext).load(Icons.getImageUrl(id)).into(holder.sourcesImageView);


        if (!TextUtils.isEmpty(name))

            holder.sourcesTextView.setText(name);

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }


    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterOnClickHandlerSources {
        void onClick(String source);
    }

    class SourcesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView sourcesImageView;
        final TextView sourcesTextView;
        final LinearLayout linearLayout;

        SourcesAdapterViewHolder(View view) {
            super(view);

            sourcesImageView = (ImageView) view.findViewById(R.id.iv_sources);
            sourcesTextView = (TextView) view.findViewById(R.id.tv_sources);
            linearLayout = (LinearLayout) view.findViewById(R.id.source_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            String name = mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_ID));
            mClickHandler.onClick(name);
        }
    }
}
