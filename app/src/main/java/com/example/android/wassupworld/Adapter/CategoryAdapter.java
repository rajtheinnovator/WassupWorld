package com.example.android.wassupworld.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder> {


    private final Context mContext;

    final private CatAdapterOnClickHandler mClickHandler;

    private ArrayList<String> mArray;
    private int lastPosition = -1;

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

    @Override
    public void onViewDetachedFromWindow(CategoryAdapterViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(CategoryAdapterViewHolder holder, int position) {

        String title = mArray.get(position);
        holder.titleTextView.setText(title.toUpperCase());
        int x = Icons.getCategroyColor(title);
        holder.backgroundImageView.setImageResource(Icons.getCategroyIcon(title));
        holder.layout.setCardBackgroundColor(ContextCompat.getColor(mContext, x));
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
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
     * The interface that receives onClick messages.
     */
    public interface CatAdapterOnClickHandler {
        void onClick(String name);
    }

    class CategoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView backgroundImageView;
        final TextView titleTextView;
        final CardView layout;

        CategoryAdapterViewHolder(View view) {
            super(view);
            layout = (CardView) view.findViewById(R.id.ll_category);
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
            int adapterPosition = getAdapterPosition();
            String name = mArray.get(adapterPosition);
            mClickHandler.onClick(name);

        }
    }
}
