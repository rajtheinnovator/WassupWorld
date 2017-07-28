package com.ayat.android.wassupworld.adapter;

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

import com.ayat.android.wassupworld.R;
import com.ayat.android.wassupworld.utils.Icons;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder> {


    private Context mContext;
    private CatAdapterOnClickHandler mClickHandler;
    private ArrayList<String> mCategoryArray;
    private int lastPosition = -1;

    public CategoryAdapter(Context context, CatAdapterOnClickHandler clickHandler, ArrayList<String> array) {
        mContext = context;
        mCategoryArray = array;
        this.mClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {

        if (mCategoryArray == null)
            return 0;
        return mCategoryArray.size();
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

        String title = mCategoryArray.get(position);
        holder.titleTextView.setText(title.toUpperCase());
        int categoryBackgroundColor = Icons.getCategoryColor(title);
        holder.backgroundImageView.setImageResource(Icons.getCategoryIcon(title));
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, categoryBackgroundColor));
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    private void setAnimation(View viewToAnimate, int position) {

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
        final CardView cardView;

        CategoryAdapterViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.ll_category);
            backgroundImageView = (ImageView) view.findViewById(R.id.iv_category_icon);
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
            String cardView = mCategoryArray.get(adapterPosition);
            mClickHandler.onClick(cardView);

        }
    }
}
