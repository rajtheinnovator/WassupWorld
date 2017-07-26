package com.example.android.wassupworld.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wassupworld.R;
import com.example.android.wassupworld.adapter.CategoryAdapter;
import com.example.android.wassupworld.utils.Icons;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements CategoryAdapter.CatAdapterOnClickHandler {

    private static final String TYPE_KEY = "type";
    private static final String TYPE_VALUE = "value";
    private static final String CATEGORY = "category";


    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ArrayList<String> categoryList = new ArrayList<>();

        categoryList.add("General");
        categoryList.add("Sport");
        categoryList.add("Gaming");
        categoryList.add("Technology");
        categoryList.add("Music");
        categoryList.add("Politics");
        categoryList.add("Science and Nature");
        categoryList.add("Entertainment");
        categoryList.add("Business");

        CategoryAdapter mCategoryAdapter = new CategoryAdapter(getContext(), this, categoryList);
        int mNoOfColumns = Icons.calculateNoOfColumnsCatList(getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), mNoOfColumns);
        RecyclerView mRecycleView = (RecyclerView) rootView.findViewById(R.id.category_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mCategoryAdapter);

       return rootView;
    }


    @Override
    public void onClick(String category) {
        Intent intent = new Intent(getActivity(), SourceNewsActivity.class);
        intent.putExtra(TYPE_KEY, CATEGORY);
        intent.putExtra(TYPE_VALUE, category);
        startActivity(intent);

    }
}
