package com.exercise.shoppingcart.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.ProductPhotosRecyclerViewAdapter;
import com.exercise.shoppingcart.models.ProductDetails;
import com.exercise.shoppingcart.models.ProductDetailsResponse;
import com.exercise.shoppingcart.models.ProductSpecifics;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.exercise.shoppingcart.utils.AppConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ProductFragment extends ProductDetailsBaseFragment {

    private LinearLayout linearLayoutProgress;
    private ScrollView scrollView;
    private TextView textViewNoResults;

    public TextView textViewTitle;
    private TextView textViewPrice;
    private TextView textViewShipping;

    private TextView textViewSubtitle;
    private TextView textViewBrand;
    private TextView textViewHighlightsPrice;

    private TextView textViewSpecifications;

    private RecyclerView recyclerViewProductImages;
    private ProductPhotosRecyclerViewAdapter recyclerViewPhotosAdapter;

    private ArrayList<String> photos;

    private IProductFragmentListener productFragmentListener;

    private LinearLayout subtitile_parent;
    private LinearLayout  price_parent;
    private LinearLayout  brand_parent;
    private LinearLayout layout_specification;

    private RelativeLayout highlights;

    private RelativeLayout divider;

    public interface IProductFragmentListener {
        void onProductDetailsResponseReceived(ProductDetailsResponse productDetailsResponse);
    }

    public ProductFragment(){

    }

    @SuppressLint("ValidFragment")
    public ProductFragment(IProductFragmentListener listener){
        productFragmentListener = listener;
    }
    SearchResponseItem selectedItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productdetails, container, false);
        linearLayoutProgress = view.findViewById(R.id.linearlayout_progress);
        scrollView = view.findViewById(R.id.scrollview_product);
        textViewNoResults = view.findViewById(R.id.textView_noresults);

        textViewTitle = view.findViewById(R.id.textView_title);
        textViewPrice = view.findViewById(R.id.textView_price);
        textViewShipping = view.findViewById(R.id.textView_shipping);
        textViewSubtitle = view.findViewById(R.id.textView_Subtitle_value);
        textViewBrand = view.findViewById(R.id.textView_Brand_value);
        textViewHighlightsPrice = view.findViewById(R.id.textView_Price_value);
        textViewSpecifications = view.findViewById(R.id.textView_specifications);
        recyclerViewProductImages = view.findViewById(R.id.recyclerView_photos);

        photos = new ArrayList<String>();
        recyclerViewPhotosAdapter = new ProductPhotosRecyclerViewAdapter(this.getActivity(),photos);
        recyclerViewProductImages.setAdapter(recyclerViewPhotosAdapter);
        recyclerViewProductImages.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL,false));

        subtitile_parent = view.findViewById(R.id.textView_Subtitle_parent);
        price_parent = view.findViewById(R.id.textView_Price_parent);
        brand_parent = view.findViewById(R.id.textView_Brand_parent);
        layout_specification = view.findViewById(R.id.linearlayout_specifications);

        highlights =  view.findViewById(R.id.relativeLayout_highlights);
        divider = view.findViewById(R.id.divider_highlight);

        String itemId = getArguments().getString("itemId");
        new ProductDetailsAsyncTask().execute(itemId);

        selectedItem = (SearchResponseItem) getArguments().getSerializable("selectedItem");

        return view;
    }

    public class ProductDetailsAsyncTask extends AsyncTask<String, Void, String>{

        private OkHttpClient okHttpClient = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearLayoutProgress.setVisibility(View.VISIBLE);
            textViewNoResults.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }

        private String prepareSpecificationString(String specifications){
            specifications = specifications.substring(1,specifications.length()-1);
            specifications = specifications.replace(',','\n');
            return specifications;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            if(responseString != null) {
                //responseString = "{\"product_details\":{\"Photo\":[\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/ouEAAOSwF21bL~Ar/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NjAwWDgwMA==/z/HPoAAOSwnpBbL-~c/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/2~IAAOSw5IFbL~Ao/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/OTAwWDEyMDA=/z/FLcAAOSwNDZbL-~S/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/54gAAOSwjiFbL~Ap/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/OTAwWDEyMDA=/z/L~oAAOSwy3dbL-~W/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/4SoAAOSwHlRbL~Ar/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/OTAwWDEyMDA=/z/ju4AAOSwSzpbL-~U/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/eicAAOSwm41bL~As/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/OTAwWDEyMDA=/z/77QAAOSwdA5bL-~X/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NzAwWDcwMA==/z/-BYAAOSw7BRbL~Ap/$_57.JPG?set_id=8800005007\",\"https://i.ebayimg.com/00/s/NjAwWDgwMA==/z/RL4AAOSwYUdbL-~b/$_57.JPG?set_id=8800005007\"],\"Title\":\"New Nike Men's Air Max Typha Training Shoe 820198\",\"Subtitle\":\"N/A\",\"Price\":\"$40\",\"Location\":\"Los Angeles, California\",\"Return_Policy\":\"N/A\",\"ItemSpecifics\":{\"Model\":\"Nike Air Max\",\"Style\":\"Athletic Sneakers\",\"Product Line\":\"Nike Air\",\"Brand\":\"Nike\"},\"seller_tab\":{\"seller_name\":\"DESISHOES2016\",\"feedback_score\":2324,\"popularity\":99.7,\"feedback_rating_star\":\"Red\",\"toprated\":\"N/A\",\"storename\":\"N/A\",\"buy_product_at\":\"N/A\"},\"facebook_link\":\"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198-/183533525754?var=\",\"google_photos\":[\"http://www.startupsummit2012.com/images/category_15/Nike%20Men%20Air%20Max%20Typha%20Training%20Shoe%20820198%20JRATGFR_1.jpg\",\"https://acrossports.s3.amazonaws.com/productPhotos/NIKE/820198608/820198608_1.jpg\",\"http://www.startupsummit2012.com/images/category_15/Nike%20Men%20Air%20Max%20Typha%20Training%20Shoe%20820198%20JRATGFR.jpg\",\"https://i.ebayimg.com/images/g/KQ0AAOSwFvBbpEV-/s-l1600.jpg\",\"http://www.startupsummit2012.com/images/category_15/Nike%20Men%20Air%20Max%20Typha%20Training%20Shoe%20820198%20JRATGFR_0.jpg\",\"https://images.nike.com/is/image/DotCom/PDP_HERO_ZOOM/820198_002_A_PREM/air-max-typha-training-shoe.jpg\",\"http://d3d71ba2asa5oz.cloudfront.net/32001347/images/820198-608.jpg\",\"https://acrossports.s3.amazonaws.com/productPhotos/NIKE/820198608/820198608_3.jpg\"],\"similar_items\": [ { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/m8IXcK_SQwPmhMVvb27_Amg.jpg\", \"title\": \"Nike Men's Air Max Typha Running Shoes 820198 010 Black/Red/White, Pre-Owned\", \"price\": 34.93, \"shipping_cost\": 0, \"days_left\": 2, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Max-Typha-Running-Shoes-820198-010-Black-Red-White-Pre-Owned/123263257378?_trkparms=%26itm%3D123263257378&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mLJJsIgWKojMQDvwnx0FNCA.jpg\", \"title\": \"Nike Men's Air Max Typha Training Shoe 820198 size 11\", \"price\": 34.99, \"shipping_cost\": 0, \"days_left\": 4, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Max-Typha-Training-Shoe-820198-size-11/233198312084?_trkparms=%26itm%3D233198312084&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mAO_gXjllIb7Ld1jmodtF-w.jpg\", \"title\": \"Size 7-13 Sneakers Men Air Cushion Shoes Outdoor Sport Casual Breathable Fitness\", \"price\": 21.84, \"shipping_cost\": 4.99, \"days_left\": 25, \"view_item_url\": \"https://www.ebay.com/itm/Size-7-13-Sneakers-Men-Air-Cushion-Shoes-Outdoor-Sport-Casual-Breathable-Fitness/153061582251?_trkparms=mehot%3Dpp%26%26itm%3D153061582251&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mx4A1eIhtI18XJrR5yEDmVg.jpg\", \"title\": \"Nike Men's Air Trainer 180 Running Shoes, 916460 100 White/Black-Black, New \", \"price\": 39.9, \"shipping_cost\": 0, \"days_left\": 21, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Trainer-180-Running-Shoes-916460-100-White-Black-Black-New/122910623396?_trkparms=%26itm%3D122910623396&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 40, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=690883356535&_trkparms=%26itm%3D690883356535&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 40, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=690883356532&_trkparms=mehot%3Dlo%26%26itm%3D690883356532&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mx6mL1UsOshaJ310xQvS2Kw.jpg\", \"title\": \"NIKE MENS AIR MAX TYPHA TRAINING SHOES #820198-608\", \"price\": 49.99, \"shipping_cost\": 0, \"days_left\": 11, \"view_item_url\": \"https://www.ebay.com/itm/NIKE-MENS-AIR-MAX-TYPHA-TRAINING-SHOES-820198-608/142992770966?_trkparms=%26itm%3D142992770966&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 40, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=691004357891&_trkparms=mehot%3Dlo%26%26itm%3D691004357891&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 40, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=691004357890&_trkparms=%26itm%3D691004357890&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mzhQ9KJpz5z9EmdjEz99xjQ.jpg\", \"title\": \"Nike Air Max Typha Men's Training Shoes 820198 002 Wolf Grey Black Dark Grey NEW\", \"price\": 59.95, \"shipping_cost\": 0, \"days_left\": 5, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Air-Max-Typha-Mens-Training-Shoes-820198-002-Wolf-Grey-Black-Dark-Grey-NEW/303035996585?_trkparms=mehot%3Dpp%26%26itm%3D303035996585&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mYHQgz5WyhYLa9DXubcd9Dw.jpg\", \"title\": \"Nike Men's Kaishi 2.0 SE Running Shoes\", \"price\": 39.99, \"shipping_cost\": 0, \"days_left\": 12, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Kaishi-2-0-SE-Running-Shoes/332824800112?_trkparms=mehot%3Dpp%26%26itm%3D332824800112&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 50, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=690883356539&_trkparms=mehot%3Dag%26%26itm%3D690883356539&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/m2P7cSmeKOWTa1Vyqs7T6KQ.jpg\", \"title\": \"Nike Men's Air Monarch IV Training Shoe\", \"price\": 48, \"shipping_cost\": 0, \"days_left\": 18, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Monarch-IV-Training-Shoe/202645205642?_trkparms=%26itm%3D202645205642&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mdtpgXDaanPQmi_LSHf4L7g.jpg\", \"title\": \"NIKE Men Flex Experience RN 7 Running Shoe 908985 002 NEW\", \"price\": 31.99, \"shipping_cost\": 0, \"days_left\": 26, \"view_item_url\": \"https://www.ebay.com/itm/NIKE-Men-Flex-Experience-RN-7-Running-Shoe-908985-002-NEW/192887536490?_trkparms=%26itm%3D192887536490&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mcZesbA2ngKy-CAvdMa9SpQ.jpg\", \"title\": \"New Nike Men's Air Max Typha Training Shoe 820198\", \"price\": 50, \"shipping_cost\": 0, \"days_left\": 27, \"view_item_url\": \"https://www.ebay.com/itm/New-Nike-Mens-Air-Max-Typha-Training-Shoe-820198/183533525754?var=691345560516&_trkparms=%26itm%3D691345560516&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mPxIvAPh6P6NMPOEyFiuWfw.jpg\", \"title\": \"Nike 820198-608 Air Max Typha Training Shoes Mens 8 Red/Black\", \"price\": 19.99, \"shipping_cost\": 0, \"days_left\": 1, \"view_item_url\": \"https://www.ebay.com/itm/Nike-820198-608-Air-Max-Typha-Training-Shoes-Mens-8-Red-Black/293042269715?_trkparms=%26itm%3D293042269715&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/msNxw6NLQej1QabPBCeg2xA.jpg\", \"title\": \"NIKE Air Max Typha Men's Training Shoes Style 820198608 Multiple Sizes\", \"price\": 49.99, \"shipping_cost\": 0, \"days_left\": 4, \"view_item_url\": \"https://www.ebay.com/itm/NIKE-Air-Max-Typha-Mens-Training-Shoes-Style-820198608-Multiple-Sizes/264248284139?_trkparms=%26itm%3D264248284139&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/muSr_fZv26OH3GX_rYBeu6A.jpg\", \"title\": \"Nike Men's Air Max Advantage Shoes\", \"price\": 49.99, \"shipping_cost\": 0, \"days_left\": 25, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Max-Advantage-Shoes/332942795358?_trkparms=mehot%3Dpp%26%26itm%3D332942795358&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/m2po3KMBS8AVolAObJRT3OA.jpg\", \"title\": \"Nike Men's Air Max Typha Training Shoes\", \"price\": 46.99, \"shipping_cost\": 0, \"days_left\": 24, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Mens-Air-Max-Typha-Training-Shoes/283100745986?_trkparms=%26itm%3D283100745986&_trksid=p0\" }, { \"photo\": \"https://securethumbs.ebay.com/d/l140/m/mHgrLlH0lPZ5R-Y8B2keEwQ.jpg\", \"title\": \"Nike Air Max Kantara Men's Running Shoes 908982 001 Black Silver White NEW!\", \"price\": 49.95, \"shipping_cost\": 0, \"days_left\": 13, \"view_item_url\": \"https://www.ebay.com/itm/Nike-Air-Max-Kantara-Mens-Running-Shoes-908982-001-Black-Silver-White-NEW/303111462140?_trkparms=mehot%3Dpp%26%26itm%3D303111462140&_trksid=p0\" } ] } }";
                linearLayoutProgress.setVisibility(View.GONE);

                Gson gson = new Gson();
                productDetailsResponse = gson.fromJson(responseString, ProductDetailsResponse.class);
                productFragmentListener.onProductDetailsResponseReceived(productDetailsResponse);

                ProductDetails productDetails = productDetailsResponse.getProduct_details();

                if (productDetails != null) {

                    textViewNoResults.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                    photos = new ArrayList<String>(Arrays.asList(productDetails.getPhoto()));
                    recyclerViewPhotosAdapter.updatePhotos(photos);

                    if(!(productDetails.getTitle() == null || productDetails.getTitle().equals("N/A"))) {
                        textViewTitle.setText(productDetails.getTitle());
                    }

                    if(!(productDetails.getPrice() == null || productDetails.getPrice().equals("N/A"))) {
                        textViewPrice.setText(productDetails.getPrice());
                        if(!(selectedItem.getShippingCost() == null || selectedItem.getShippingCost().equals("N/A"))) {
                            textViewShipping.setText(" With " + selectedItem.getShippingCost());
                        }
                    }

                    boolean hasHighlights = false;

                    if(!(selectedItem.getSubtitle() == null || selectedItem.getSubtitle().equals("N/A"))) {
                        hasHighlights = true;
                        textViewSubtitle.setText(selectedItem.getSubtitle());
                    } else {
                        subtitile_parent.setVisibility(View.GONE);
                    }

                    if(!(productDetails.getPrice() == null || productDetails.getPrice().equals("N/A"))) {
                        hasHighlights = true;
                        textViewHighlightsPrice.setText(productDetails.getPrice());
                    } else {
                        price_parent.setVisibility(View.GONE);
                    }

                    Map<String, String> rows = productDetails.getItemSpecifics();
                    StringBuilder specifications = new StringBuilder();
                    String brand = rows.get("Brand");
                    if(!(brand == null || brand.equals("N/A"))) {
                        hasHighlights = true;
                        textViewBrand.setText(rows.get("Brand"));
                        specifications.append("\u2022").append(" " + rows.get("Brand")).append("<br>");
                    } else {
                        brand_parent.setVisibility(View.GONE);
                    }

                    if(!hasHighlights) {
                        highlights.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                    }

                    for (String key : rows.keySet()) {
                        if (!key.equals("Brand")) {
                            String detail = rows.get(key);
                            String capDetail = detail.substring(0, 1).toUpperCase() + detail.substring(1);
                            specifications.append("\u2022").append(" " + capDetail).append("<br>");
                        }
                    }

                    if(specifications.length() > 0) {
                        textViewSpecifications.setText(Html.fromHtml(specifications.toString()));
                    } else {
                        layout_specification.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                    }
                } else {
                    textViewNoResults.setTextSize(20);
                    textViewNoResults.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getActivity(), String.format("Possible network error in fetching search results"), Toast.LENGTH_LONG).show();
                linearLayoutProgress.setVisibility(View.GONE);
                textViewNoResults.setTextSize(20);
                textViewNoResults.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        }

        @Override
        protected String doInBackground(String... itemIds) {
            Request.Builder builder = new Request.Builder();
            builder.url(AppConstants.BASE_URL + AppConstants.DETAILS_ENDPOINT + String.format("?itemId=%s",itemIds[0]));
            Request request = builder.build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e){

            }
            return null;
        }
    }
}
