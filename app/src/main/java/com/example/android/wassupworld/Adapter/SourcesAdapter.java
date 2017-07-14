package com.example.android.wassupworld.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;
import com.example.android.wassupworld.provider.NewsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 7/5/2017.
 */

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesAdapterViewHolder> {


    private Cursor mCursor;
    private final Context mContext;

    final private AdapterOnClickHandlerSources mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterOnClickHandlerSources {
        void onClick(String source);
    }

    public SourcesAdapter(Context context, Cursor cursor, AdapterOnClickHandlerSources clickHandler) {

        mContext = context;
        mCursor = cursor;
        this.mClickHandler = clickHandler;
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
String id =mCursor.getString(mCursor.getColumnIndex(NewsContract.SourcesEntry.COLUMN_ID));
        if (!TextUtils.isEmpty(logoUrl))
            Picasso.with(mContext).load(logoUrl).into(holder.sourcesImageView);
else
         Picasso.with(mContext).load(Icons.getImageUrl(id)).into(holder.sourcesImageView);


        if(!TextUtils.isEmpty(name))

        holder.sourcesTextView.setText(name);


    }


    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    class SourcesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView sourcesImageView;
        final TextView sourcesTextView;


        SourcesAdapterViewHolder(View view) {
            super(view);

            sourcesImageView = (ImageView) view.findViewById(R.id.iv_sources);
            sourcesTextView = (TextView) view.findViewById(R.id.tv_sources);

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
