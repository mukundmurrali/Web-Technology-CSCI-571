package com.exercise.shoppingcart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.fragments.SearchResultsFragment;
import com.exercise.shoppingcart.models.SearchQuery;
import com.exercise.shoppingcart.models.SearchResponseItem;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsFragment.ISearchResultsFragmentListener{

    private SearchQuery searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchQuery = (SearchQuery) getIntent().getExtras().getSerializable("SearchQuery");

        loadSearchResultsFragment();
    }

    private void loadSearchResultsFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("searchQuery",searchQuery);
        searchResultsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.framelayout_searchresults, searchResultsFragment, "SearchResultsFragment");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemSelected(SearchResponseItem searchResponseItem) {
        Intent intent = new Intent(this,com.exercise.shoppingcart.activity.ProductDetailsActivity.class);
        intent.putExtra("SelectedItem", searchResponseItem);
        startActivity(intent);
    }
}
