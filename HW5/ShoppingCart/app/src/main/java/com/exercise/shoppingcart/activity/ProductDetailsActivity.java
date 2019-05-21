package com.exercise.shoppingcart.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.fragments.GooglePhotosFragment;
import com.exercise.shoppingcart.fragments.ProductFragment;
import com.exercise.shoppingcart.fragments.SearchFragment;
import com.exercise.shoppingcart.fragments.ShippingFragment;
import com.exercise.shoppingcart.fragments.SimilarItemsFragment;
import com.exercise.shoppingcart.fragments.WishlistFragment;
import com.exercise.shoppingcart.models.ProductDetailsResponse;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.exercise.shoppingcart.models.SimilarItems;
import com.exercise.shoppingcart.utils.SharedPreferenceManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity implements ProductFragment.IProductFragmentListener, SimilarItemsFragment.ISimilarItemsFragmentListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SearchResponseItem selectedItem;
    private ProductDetailsResponse productDetailsResponse;

    private ProductFragment productFragment;
    private ShippingFragment shippingFragment;
    private GooglePhotosFragment googlePhotosFragment;
    private SimilarItemsFragment similarItemsFragment;

    private ShareDialog shareDialog;
    private CallbackManager callbackManager;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        selectedItem = (SearchResponseItem) getIntent().getExtras().getSerializable("SelectedItem");
        setTitle(selectedItem.getTitle());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        setupTabIcons();
        setupWishlistFabIcon();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    private boolean isItemPresentInWishList(){
        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this);
        final ArrayList<SearchResponseItem> wishlistItems = sharedPreferenceManager.getWishlistedItems();
        if(wishlistItems == null){
            return false;
        }
        Iterator<SearchResponseItem> iterator = wishlistItems.iterator();
        while(iterator.hasNext()){
            if(selectedItem.getItemId().equals(iterator.next().getItemId())){
                return true;
            }
        }
        return false;
    }

    private void setupWishlistFabIcon(){

        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this);

        if(!isItemPresentInWishList()){
            fab.setImageResource(R.drawable.ic_cart_plus_white);
        } else {
            fab.setImageResource(R.drawable.ic_cart_remove_white);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lines = productFragment.textViewTitle.getLineCount();
                String sentence = productFragment.textViewTitle.getText().toString();
                if (sentence.length() > 40) {
                    int end = productFragment.textViewTitle.getLayout().getLineEnd(productFragment.textViewTitle.getMaxLines() - 1);
                    sentence = productFragment.textViewTitle.getText().toString().substring(0, 40) + "...";
                }
                if(!isItemPresentInWishList()){
                    Toast toast = Toast.makeText(view.getContext(), String.format("%s was added to wish list", sentence), Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(13);
                    toast.show();
                    sharedPreferenceManager.addItemToWishlist(selectedItem);
                    fab.setImageResource(R.drawable.ic_cart_remove_white);
                } else {
                    Toast toast = Toast.makeText(view.getContext(), String.format("%s was removed from wish list", sentence), Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(13);
                    toast.show();
                    sharedPreferenceManager.removeItemFromWishlist(selectedItem);
                    fab.setImageResource(R.drawable.ic_cart_plus_white);
                }
            }
        });

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("PRODUCT");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_information_variant, 0, 0);

        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("SHIPPING");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_truck_delivery, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("PHOTOS");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_google, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);


        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("SIMILAR");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_equal, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabLayout.getTabAt(0).getCustomView().setAlpha(1);
        tabLayout.getTabAt(1).getCustomView().setAlpha(0.6f);
        tabLayout.getTabAt(2).getCustomView().setAlpha(0.6f);
        tabLayout.getTabAt(3).getCustomView().setAlpha(0.6f);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabLayout.getTabAt(0).getCustomView().setAlpha(1);
                        tabLayout.getTabAt(1).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(2).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(3).getCustomView().setAlpha(0.6f);

                        break;
                    case 1:
                        tabLayout.getTabAt(0).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(1).getCustomView().setAlpha(1);
                        tabLayout.getTabAt(2).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(3).getCustomView().setAlpha(0.6f);

                        break;
                    case 2:
                        tabLayout.getTabAt(0).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(1).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(2).getCustomView().setAlpha(1);
                        tabLayout.getTabAt(3).getCustomView().setAlpha(0.6f);
                        break;
                    case 3:
                        tabLayout.getTabAt(0).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(1).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(2).getCustomView().setAlpha(0.6f);
                        tabLayout.getTabAt(3).getCustomView().setAlpha(1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productdetails,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuitem_facebook:
                onFacebookIconClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onFacebookIconClicked(){
        // String productUrl = Uri.encode("https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198-/183533525754?var=");
        ShareLinkToFacebookWithSDK(productDetailsResponse.getProduct_details().getFacebook_link(),
                productDetailsResponse.getProduct_details().getTitle(),
                productDetailsResponse.getProduct_details().getPrice());
    }

    private void ShareLinkToFacebookInBrowser(String productUrl){
        productUrl = Uri.encode(productUrl);
        String facebookShareUrl = "https://www.facebook.com/dialog/share?" +
                "app_id=2226971704285763" +
                "&display=popup" +
                "&href=" + productUrl +
                "&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareUrl));
        startActivity(browserIntent);
    }

    private void ShareLinkToFacebookWithSDK(String productUrl, String title, String price){
        Uri shareUri = Uri.parse(productUrl);
        String postContent = String.format("Buy %s for %s from ebay!",title, price);
        ShareHashtag shareHashtag = new ShareHashtag.Builder()
                .setHashtag("#CSCI571Spring2019Ebay")
                .build();
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setQuote(postContent)
                .setContentUrl(shareUri)
                .setShareHashtag(shareHashtag)
                .build();
        if(ShareDialog.canShow(ShareLinkContent.class)){
            shareDialog.show(shareLinkContent);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ProductDetailsViewPagerAdapter adapter = new ProductDetailsViewPagerAdapter(getSupportFragmentManager());

        productFragment = new ProductFragment(this);
        Bundle bundle = new Bundle();
        bundle.putString("itemId", selectedItem.getItemId());
        bundle.putSerializable("selectedItem", selectedItem);
        productFragment.setArguments(bundle);
        adapter.addFragment(productFragment, "PRODUCT");

        shippingFragment = new ShippingFragment();
        Bundle shippingBundle = new Bundle();
        shippingBundle.putSerializable("selectedItem", selectedItem);
        shippingFragment.setArguments(shippingBundle);
        adapter.addFragment(shippingFragment, "SHIPPING");

        googlePhotosFragment = new GooglePhotosFragment();
        adapter.addFragment(googlePhotosFragment, "PHOTOS");

        similarItemsFragment = new SimilarItemsFragment(this);
        adapter.addFragment(similarItemsFragment, "SIMILAR");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onProductDetailsResponseReceived(ProductDetailsResponse productDetailsResponse) {
        this.productDetailsResponse = productDetailsResponse;
        shippingFragment.setProductDetailsResponse(productDetailsResponse);
        googlePhotosFragment.setProductDetailsResponse(productDetailsResponse);
        similarItemsFragment.setProductDetailsResponse(productDetailsResponse);
    }

    @Override
    public void onSimilarItemClicked(SimilarItems item) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(item.getView_item_url()));
        startActivity(browserIntent);
    }

    class ProductDetailsViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ProductDetailsViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
