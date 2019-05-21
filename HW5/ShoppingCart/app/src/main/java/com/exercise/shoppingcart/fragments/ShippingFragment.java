package com.exercise.shoppingcart.fragments;

import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.models.ProductDetails;
import com.exercise.shoppingcart.models.ProductDetailsResponse;
import com.exercise.shoppingcart.models.ReturnPolicy;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.exercise.shoppingcart.models.SellerTab;
import com.exercise.shoppingcart.models.ShippingTab;
import com.wssholmes.stark.circular_score.CircularScoreView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ShippingFragment extends ProductDetailsBaseFragment {

    private static final Map<String, String> colorRGBMap = new HashMap<>();
    private TextView textViewStoreName;
    private TextView textViewFeedbackScore;
    private TextView textViewShippingCost;
    private TextView textViewGlobalShipping;
    private TextView textViewHandlingTime;
    private TextView textViewCondition;
    private TextView textViewPolicy;
    private TextView textViewReturnsWithin;
    private TextView textViewRefundMode;
    private TextView textViewShippedBy;

    private LinearLayout soldbyLayout;
    private LinearLayout soldbyHeader;
    private LinearLayout shippingLayout;
    private LinearLayout shippingHeader;
    private LinearLayout returnPolicyLayout;
    private LinearLayout returnPolicyHeader;
    private RelativeLayout divider1;
    private RelativeLayout divider2;

    private LinearLayout storename;
    private LinearLayout feedbackscore;
    private LinearLayout popularity;
    private LinearLayout feedbackstar;

    private LinearLayout shippingCost;
    private LinearLayout globalShipping;
    private LinearLayout handlingTime;
    private LinearLayout condition;

    private LinearLayout returnPolicy;
    private LinearLayout returnWithin;
    private LinearLayout refundMode;
    private LinearLayout shippedBy;

    private LinearLayout layout_parent;

    private TextView noresults_shipping;
    private ImageView feedbackStar;
    private CircularScoreView circularScoreView;

    SearchResponseItem selectedItem;

    static {
        colorRGBMap.put("GreenShooting","0,255,0");
        colorRGBMap.put("Green","0,255,0");

        colorRGBMap.put("Purple","128,0,128");
        colorRGBMap.put("PurpleShooting","128,0,128");

        colorRGBMap.put("Red","255,0,0");
        colorRGBMap.put("RedShooting","255,0,0");

        colorRGBMap.put("SilverShooting","192,192,192");

        colorRGBMap.put("Turquoise","64,224,208");
        colorRGBMap.put("TurquoiseShooting","64,224,208");

        colorRGBMap.put("Yellow","255,255,0");
        colorRGBMap.put("YellowShooting","255,255,0");

        colorRGBMap.put("Blue","0,0,255");



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping,container,false);

        textViewStoreName = view.findViewById(R.id.textView_storename);
        textViewFeedbackScore = view.findViewById(R.id.textView_feedbackscore);
        textViewShippingCost = view.findViewById(R.id.textview_shippingcost);
        textViewGlobalShipping = view.findViewById(R.id.textview_globalshipping);
        textViewHandlingTime = view.findViewById(R.id.textView_handlingTime);
        textViewCondition = view.findViewById(R.id.textView_condition);
        textViewPolicy = view.findViewById(R.id.textView_policy);
        textViewReturnsWithin = view.findViewById(R.id.textView_returnswithin);
        textViewRefundMode = view.findViewById(R.id.textView_refundmode);
        textViewShippedBy = view.findViewById(R.id.textView_shippedby);

        soldbyHeader = view.findViewById(R.id.linearlayout_soldby_header);
        soldbyLayout = view.findViewById(R.id.linearlayout_soldby_content);
        shippingHeader = view.findViewById(R.id.linearlayout_shipping_header);
        shippingLayout = view.findViewById(R.id.linearlayout_shipping_content);
        returnPolicyHeader = view.findViewById(R.id.linearlayout_return_header);
        returnPolicyLayout = view.findViewById(R.id.linearlayout_return_content);
        divider1 = view.findViewById(R.id.divider1);
        divider2 = view.findViewById(R.id.divider2);

        storename = view.findViewById(R.id.ll_storename);
        feedbackscore = view.findViewById(R.id.ll_feedbackscore);
        popularity = view.findViewById(R.id.ll_popularity);
        feedbackstar = view.findViewById(R.id.ll_feedbackstar);

        shippingCost = view.findViewById(R.id.ll_shippingcost);
        globalShipping = view.findViewById(R.id.ll_globalshipping);
        handlingTime = view.findViewById(R.id.ll_handlingtime);
        condition = view.findViewById(R.id.ll_condition);

        returnPolicy = view.findViewById(R.id.ll_policy);
        returnWithin = view.findViewById(R.id.ll_returnswhen);
        refundMode = view.findViewById(R.id.ll_refundmode);
        shippedBy = view.findViewById(R.id.ll_shippedby);


        layout_parent = view.findViewById(R.id.linearlayout_parent);
        noresults_shipping = view.findViewById(R.id.textView_noresults_shipping);

        circularScoreView = view.findViewById(R.id.circularScoreView);
        feedbackStar = view.findViewById(R.id.feedback_star);
        selectedItem = (SearchResponseItem) getArguments().getSerializable("selectedItem");
        return view;
    }

    @Override
    protected void updateFragmentContent() {
        super.updateFragmentContent();
        if(productDetailsResponse != null) {
            ProductDetails productDetails = productDetailsResponse.getProduct_details();

            ShippingTab shippingTab = productDetails.getShippingTab();


            /*****************************************************************************************/
            boolean hasShipping = false;
            if(!(selectedItem.getShippingCost() == null || selectedItem.getShippingCost().equals("N/A"))) {
                textViewShippingCost.setText(selectedItem.getShippingCost());
                hasShipping = true;
            } else {
                shippingCost.setVisibility(View.GONE);
            }

            if(!(shippingTab.getGlobal_shipping() == null || shippingTab.getGlobal_shipping().equals("N/A"))) {
                if (shippingTab.getGlobal_shipping().equals("true")) {
                    textViewGlobalShipping.setText("Yes");
                } else {
                    textViewGlobalShipping.setText("No");
                }
                hasShipping = true;
            } else {
                globalShipping.setVisibility(View.GONE);
            }

            if(!(shippingTab.getHandling_time() == null || shippingTab.getHandling_time().equals("N/A"))) {
                hasShipping = true;
                textViewHandlingTime.setText(shippingTab.getHandling_time());
            } else {
                handlingTime.setVisibility(View.GONE);
            }

            if(!(shippingTab.getCondition() == null || shippingTab.getCondition().equals("N/A"))) {
                hasShipping = true;
                textViewCondition.setText(shippingTab.getCondition());
            } else {
                condition.setVisibility(View.GONE);
            }

            if(!hasShipping) {
                soldbyHeader.setVisibility(View.GONE);
                soldbyLayout.setVisibility(View.GONE);
                divider1.setVisibility(View.GONE);
            }

            /*****************************************************************************************/

            SellerTab sellerTab = productDetails.getSeller_tab();
            boolean hasSellerInfo = false;
            if(!(sellerTab.getStorename() == null || sellerTab.getStorename().equals("N/A"))) {
                textViewStoreName.setClickable(true);
                textViewStoreName.setMovementMethod(LinkMovementMethod.getInstance());
                String hyperlink = "<a href='" + productDetails.getSeller_tab().getBuy_product_at() + "'>" + sellerTab.getStorename() + "</a>";
                textViewStoreName.setText(Html.fromHtml(hyperlink));
                hasSellerInfo = true;
            } else {
                storename.setVisibility(View.GONE);
            }


            if(!(sellerTab.getFeedback_score() == null || sellerTab.getFeedback_score().equals("N/A"))) {
                textViewFeedbackScore.setText(sellerTab.getFeedback_score());
                hasSellerInfo = true;
            } else {
                feedbackscore.setVisibility(View.GONE);
            }

            if(!(sellerTab.getPopularity() == null || sellerTab.getPopularity().equals("N/A"))) {
                circularScoreView.setScore((int) Double.parseDouble(sellerTab.getPopularity()));
                hasSellerInfo = true;
            } else {
                popularity.setVisibility(View.GONE);
            }

            String starValue = productDetails.getSeller_tab().getFeedback_rating_star();
            if(!(starValue == null || starValue.equals("N/A") || starValue.equals("None"))) {
                hasSellerInfo = true;
                String[] rgb = colorRGBMap.get(starValue).split(",");
                if (starValue.contains("Shooting")) {
                    feedbackStar.setColorFilter(android.graphics.Color.rgb(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2])));
                } else {
                    feedbackStar.setImageResource(R.drawable.ic_star_circle_outline);
                    feedbackStar.setColorFilter(android.graphics.Color.rgb(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2])));
                }
            } else {
                feedbackstar.setVisibility(View.GONE);
            }

            if(!hasSellerInfo) {
                shippingHeader.setVisibility(View.GONE);
                shippingLayout.setVisibility(View.GONE);
                divider2.setVisibility(View.GONE);
            }

            /*****************************************************************************************/
            ReturnPolicy policy = productDetails.getReturn_Policy();

            boolean hasPolicy = false;
            if(!(policy.getPolicy() == null || policy.getPolicy().equals("N/A"))) {
                hasPolicy = true;
                textViewPolicy.setText(policy.getPolicy());
            } else {
                returnPolicy.setVisibility(View.GONE);
            }

            if(!(policy.getReturns_within() == null || policy.getReturns_within().equals("N/A"))) {
                hasPolicy = true;
                textViewReturnsWithin.setText(policy.getReturns_within());
            } else {
                returnWithin.setVisibility(View.GONE);
            }

            if(!(policy.getRefund_mode() == null || policy.getRefund_mode().equals("N/A"))) {
                hasPolicy = true;
                textViewRefundMode.setText(policy.getRefund_mode());
            } else {
                refundMode.setVisibility(View.GONE);
            }

            if(!(policy.getShipped_by() == null || policy.getShipped_by().equals("N/A"))) {
                hasPolicy = true;
                textViewShippedBy.setText(policy.getShipped_by());
            } else {
                shippedBy.setVisibility(View.GONE);
            }

            if(!hasPolicy) {
                returnPolicyLayout.setVisibility(View.GONE);
                returnPolicyHeader.setVisibility(View.GONE);
                divider2.setVisibility(View.GONE);
            }

            if(!hasPolicy && !hasSellerInfo && !hasShipping) {
                layout_parent.setVisibility(View.GONE);
                noresults_shipping.setVisibility(View.VISIBLE);
            }

        } else {
            layout_parent.setVisibility(View.GONE);
            noresults_shipping.setVisibility(View.VISIBLE);
        }

    }
}
