package com.example.android.wassupworld.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wassupworld.Adapter.CategoryAdapter;
import com.example.android.wassupworld.R;
import com.example.android.wassupworld.Utils.Icons;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements CategoryAdapter.CatAdapterOnClickHandler {
    private RecyclerView mRecycleView;
    private GridLayoutManager mLayoutManager;
    private CategoryAdapter mCategoryAdapter;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
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

        mCategoryAdapter = new CategoryAdapter(getContext() , this,categoryList);
        int mNoOfColumns = Icons.calculateNoOfColumnsCatList(getContext());
        mLayoutManager = new GridLayoutManager(getContext(),mNoOfColumns);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.news_list_recycle_view);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mCategoryAdapter);

       return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(String url) {

    }
}
