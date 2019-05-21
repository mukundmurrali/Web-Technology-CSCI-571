package com.exercise.shoppingcart.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.GooglePhotosRecyclerViewAdapter;
import com.exercise.shoppingcart.layoutManagers.SearchResultsItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class GooglePhotosFragment extends ProductDetailsBaseFragment {

    private ArrayList<String> googlePhotos;

    private RecyclerView recyclerViewGooglePhotos;
    private TextView textViewNoGooglePhotos;
    private GooglePhotosRecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_googlephotos, container, false);
        recyclerViewGooglePhotos = view.findViewById(R.id.recyclerView_googlephotos);
        textViewNoGooglePhotos = view.findViewById(R.id.textView_nogooglephotos);
        googlePhotos = new ArrayList<>();

        recyclerViewAdapter = new GooglePhotosRecyclerViewAdapter(this.getActivity(), googlePhotos);
        recyclerViewGooglePhotos.setAdapter(recyclerViewAdapter);
        recyclerViewGooglePhotos.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerViewGooglePhotos.addItemDecoration(new SearchResultsItemDecoration(spacingInPixels));

        return view;
    }

    @Override
    protected void updateFragmentContent() {
        super.updateFragmentContent();
        if(productDetailsResponse != null){
            googlePhotos = new ArrayList<>(Arrays.asList(productDetailsResponse.getProduct_details().getGoogle_photos()));
            if(googlePhotos.size() == 0){
                textViewNoGooglePhotos.setVisibility(View.VISIBLE);
                recyclerViewGooglePhotos.setVisibility(View.GONE);
            } else {
                textViewNoGooglePhotos.setVisibility(View.GONE);
                recyclerViewGooglePhotos.setVisibility(View.VISIBLE);
                recyclerViewAdapter.updatePhotos(googlePhotos);
            }
        } else {
            textViewNoGooglePhotos.setVisibility(View.VISIBLE);
            recyclerViewGooglePhotos.setVisibility(View.GONE);
        }
    }
}
