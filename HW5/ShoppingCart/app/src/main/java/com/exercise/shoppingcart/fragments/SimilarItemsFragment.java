package com.exercise.shoppingcart.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.SimilarItemsRecyclerViewAdapter;
import com.exercise.shoppingcart.layoutManagers.SearchResultsItemDecoration;
import com.exercise.shoppingcart.models.SimilarItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class SimilarItemsFragment extends ProductDetailsBaseFragment implements SimilarItemsRecyclerViewAdapter.ISimilarItemsRecyclerViewAdapterListener {

    private ISimilarItemsFragmentListener mListener;
    private Spinner spinnerSortby;
    private Spinner spinnerSortOrder;
    private TextView textViewNoSimilarItems;
    private RecyclerView recyclerViewSimilarItems;

    private SimilarItemsRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<SimilarItems> similarItems;

    public interface ISimilarItemsFragmentListener{
        void onSimilarItemClicked(SimilarItems item);
    }

    public SimilarItemsFragment(){

    }

    @SuppressLint("ValidFragment")
    public SimilarItemsFragment(ISimilarItemsFragmentListener listener){
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_similaritems, container, false);

        spinnerSortby = view.findViewById(R.id.spinner_sortby);
        spinnerSortOrder = view.findViewById(R.id.spinner_sortorder);
        textViewNoSimilarItems = view.findViewById(R.id.textView_nosimilaritems);
        recyclerViewSimilarItems = view.findViewById(R.id.recyclerView_similarItems);

        similarItems = new ArrayList<>();
        recyclerViewAdapter =  new SimilarItemsRecyclerViewAdapter(this.getActivity(),similarItems, this);
        recyclerViewSimilarItems.setAdapter(recyclerViewAdapter);
        recyclerViewSimilarItems.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerViewSimilarItems.addItemDecoration(new SearchResultsItemDecoration(spacingInPixels));

        spinnerSortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortSimilarItems(position, spinnerSortOrder.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortSimilarItems(spinnerSortby.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateFragmentContent();

        return view;
    }

    private void sortSimilarItems(int position, int order){
        if(productDetailsResponse != null && productDetailsResponse.getProduct_details().getSimilar_items().length > 0){
            switch(position){
                case 0:
                    spinnerSortOrder.setEnabled(false);
                    displayInDefaultOrder(order);
                    break;
                case 1:
                    spinnerSortOrder.setEnabled(true);
                    sortByName(order);
                    break;
                case 2:
                    spinnerSortOrder.setEnabled(true);
                    sortByPrice(order);
                    break;
                case 3:
                    spinnerSortOrder.setEnabled(true);
                    sortByDaysLeft(order);
                    break;
                default:
                    spinnerSortOrder.setEnabled(false);
                    displayInDefaultOrder(order);
                    break;
            }
        }
    }

    private void displayInDefaultOrder(int order){
        similarItems = new ArrayList<>(Arrays.asList(productDetailsResponse.getProduct_details().getSimilar_items()));
        recyclerViewAdapter.updateSimilarItems(similarItems);
    }

    private void sortByName(final int order){
        Collections.sort(similarItems, new Comparator<SimilarItems>() {
            @Override
            public int compare(SimilarItems obj1, SimilarItems obj2) {
                if(order == 0){
                    return obj1.getTitle().compareTo(obj2.getTitle());
                } else if (order == 1) {
                    return obj2.getTitle().compareTo(obj1.getTitle());
                }
                return 0;
            }
        });
        recyclerViewAdapter.updateSimilarItems(similarItems);
    }

    private void sortByPrice(final int order){
        Collections.sort(similarItems, new Comparator<SimilarItems>() {
            @Override
            public int compare(SimilarItems obj1, SimilarItems obj2) {
                if(order == 0){
                    return Double.valueOf(obj1.getPrice()).compareTo(Double.valueOf(obj2.getPrice()));
                } else if (order == 1) {
                    return Double.valueOf(obj2.getPrice()).compareTo(Double.valueOf(obj1.getPrice()));
                }
                return 0;
            }
        });
        recyclerViewAdapter.updateSimilarItems(similarItems);
    }

    private void sortByDaysLeft(final int order){
        Collections.sort(similarItems, new Comparator<SimilarItems>() {
            @Override
            public int compare(SimilarItems obj1, SimilarItems obj2) {
                if(order == 0){
                    return Integer.valueOf(obj1.getDays_left()).compareTo(Integer.valueOf(obj2.getDays_left()));
                } else if (order == 1) {
                    return Integer.valueOf(obj2.getDays_left()).compareTo(Integer.valueOf(obj1.getDays_left()));
                }
                return 0;
            }
        });
        recyclerViewAdapter.updateSimilarItems(similarItems);
    }

    @Override
    protected void updateFragmentContent() {
        if(productDetailsResponse != null){
            similarItems = new ArrayList<SimilarItems>(Arrays.asList(productDetailsResponse.getProduct_details().getSimilar_items()));
            if(similarItems.size() == 0){
                textViewNoSimilarItems.setVisibility(View.VISIBLE);
                spinnerSortOrder.setEnabled(false);
                spinnerSortby.setEnabled(false);
                recyclerViewSimilarItems.setVisibility(View.GONE);
            } else {
                textViewNoSimilarItems.setVisibility(View.GONE);
                spinnerSortOrder.setEnabled(false);
                spinnerSortby.setEnabled(true);
                recyclerViewSimilarItems.setVisibility(View.VISIBLE);
                recyclerViewAdapter.updateSimilarItems(similarItems);
            }
        } else {
            textViewNoSimilarItems.setVisibility(View.VISIBLE);
            spinnerSortOrder.setEnabled(false);
            spinnerSortby.setEnabled(false);
            recyclerViewSimilarItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(SimilarItems item) {
        mListener.onSimilarItemClicked(item);
    }
}
