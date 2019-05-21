package com.exercise.shoppingcart.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.AutoCompleteTextViewAdapter;
import com.exercise.shoppingcart.models.SearchQuery;
import com.exercise.shoppingcart.utils.ApiManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mukundmurrali on 4/9/2019.
 */

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private Handler handler;

    private EditText editTextKeyword;
    private Spinner spinnerCategory;
    private CheckBox checkBoxNearby;
    private CheckBox checkBoxNew;
    private CheckBox checkBoxUsed;
    private CheckBox checkBoxUnspecified;
    private CheckBox checkBoxLocalPickup;
    private CheckBox checkBoxFreeShipping;
    private RelativeLayout relativeLayoutNearby;
    private EditText editTextMilesFrom;
    private RadioButton radioButtonCurrentLocation;
    private RadioButton radioButtonZipcode;
    private AutoCompleteTextView autoCompleteTextViewZipcode;
    private Button buttonSearch;
    private Button buttonClear;
    private TextInputLayout text_input_layout_keyword;
    private TextInputLayout text_input_layout_autocomplete;

    private View view;

    private int currentLocationZipCode;

    private AutoCompleteTextViewAdapter autoCompleteTextViewAdapter;

    private ISearchFragmentListener _searchFragmentListener;

    public interface ISearchFragmentListener {
        public void OnSearchButtonClicked(SearchQuery searchQuery);
        public void OnCurrentLocationSelected();
    }

    public SearchFragment(){

    }

    @SuppressLint("ValidFragment")
    public SearchFragment(ISearchFragmentListener searchFragmentListener){
        _searchFragmentListener = searchFragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        editTextKeyword = view.findViewById(R.id.editText_keyword);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        checkBoxNew = view.findViewById(R.id.checkbox_New);
        checkBoxUsed = view.findViewById(R.id.checkBox_Used);
        checkBoxUnspecified = view.findViewById(R.id.checkbox_Unspecified);
        checkBoxLocalPickup = view.findViewById(R.id.checkbox_local);
        checkBoxFreeShipping = view.findViewById(R.id.checkbox_freeShipping);
        checkBoxNearby = view.findViewById(R.id.checkBox_nearby);
        relativeLayoutNearby = view.findViewById(R.id.relativeLayout_nearby);
        editTextMilesFrom = view.findViewById(R.id.editText_MilesFrom);
        radioButtonCurrentLocation = view.findViewById(R.id.radioButton_currentlocation);
        radioButtonZipcode = view.findViewById(R.id.radioButton_zip);
        autoCompleteTextViewZipcode = view.findViewById(R.id.autoCompleteTextView_zip);
        buttonSearch = view.findViewById(R.id.button_search);
        buttonClear = view.findViewById(R.id.button_clear);
        text_input_layout_keyword = (TextInputLayout) view.findViewById(R.id.keyword_field_parent);
        text_input_layout_autocomplete = (TextInputLayout) view.findViewById(R.id.autocomplete_field_parent);

        // Set array adapter for spinner
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.category_options,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(arrayAdapter);

        checkBoxNearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    relativeLayoutNearby.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutNearby.setVisibility(View.GONE);
                }
            }
        });

        radioButtonCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioButtonZipcode.setChecked(false);
                    autoCompleteTextViewZipcode.setEnabled(false);
                }
            }
        });
        radioButtonZipcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioButtonCurrentLocation.setChecked(false);
                    autoCompleteTextViewZipcode.setEnabled(true);
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllFields();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchButtonClick();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (autoCompleteTextViewZipcode.getText() != null && !autoCompleteTextViewZipcode.getText().toString().isEmpty()) {
                        getZipCodeFromRestAPI(autoCompleteTextViewZipcode.getText().toString());
                    }
                }
                return false;
            }
        });

        autoCompleteTextViewAdapter = new AutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextViewZipcode.setThreshold(3);
        autoCompleteTextViewZipcode.setAdapter(autoCompleteTextViewAdapter);
        autoCompleteTextViewZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void getZipCodeFromRestAPI(String enteredText){
        ApiManager.call(this.getActivity(), enteredText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> zipList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++){
                        zipList.add(array.getString(i));
                    }
                    autoCompleteTextViewAdapter.setData(zipList);
                } catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast =  Toast.makeText(getActivity(), String.format("Possible network error in getting zipcodes"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public void setCurrentLocationZipCode( int currentLocationZipCode){
        this.currentLocationZipCode = currentLocationZipCode;
    }

    private int getCategoryCode(String categoryName) {
        List<String> categoryOptions = Arrays.asList(getResources().getStringArray(R.array.category_options));
        List<Integer> categoryCodes = Arrays.asList(new Integer[] { 0, 550, 2984, 267, 11450, 58058, 26395, 11233, 1249});
        int index = categoryOptions.indexOf(categoryName);
        return categoryCodes.get(index);
    }

    private void handleSearchButtonClick(){
        boolean validationSuccess = true;

        SearchQuery searchQuery = new SearchQuery();

        Editable keyword_text = editTextKeyword.getText();
        String keyword_text_field = null;
        if(keyword_text != null) {
            keyword_text_field = keyword_text.toString().trim();
        }
        if(keyword_text_field == null || keyword_text_field.length() == 0) {
            text_input_layout_keyword.setError("Please enter mandatory field");
            text_input_layout_keyword.setErrorTextAppearance(R.style.ErrorSize);
            text_input_layout_keyword.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.crimson)));
            validationSuccess = false;
        } else {
            searchQuery.setKeyword(editTextKeyword.getText().toString());
            text_input_layout_keyword.setError("");
            text_input_layout_keyword.setErrorTextAppearance(R.style.ErrorSizeZero);
        }

        searchQuery.setCategory(getCategoryCode(spinnerCategory.getSelectedItem().toString()));

        searchQuery.setNew(checkBoxNew.isChecked());
        searchQuery.setUsed(checkBoxUsed.isChecked());
        searchQuery.setUnspecified(checkBoxUnspecified.isChecked());

        searchQuery.setFreeShipping(checkBoxFreeShipping.isChecked());
        searchQuery.setLocalPickup(checkBoxLocalPickup.isChecked());

        searchQuery.setNearby(checkBoxNearby.isChecked());

        if(checkBoxNearby.isChecked()){
            searchQuery.setNearby(true);
            if(editTextMilesFrom.getText().toString().equals("")) {
                searchQuery.setMiles(10);
            } else {
                searchQuery.setMiles(Integer.parseInt(editTextMilesFrom.getText().toString()));
            }
            if(radioButtonZipcode.isChecked()){
                searchQuery.setDistance("zip");
                Editable zipcode_text = autoCompleteTextViewZipcode.getText();
                String zipcode_text_field = null;
                if(zipcode_text != null) {
                    zipcode_text_field = zipcode_text.toString().trim();
                }
                if(zipcode_text_field == null || zipcode_text_field.length() == 0) {
                    text_input_layout_autocomplete.setError("Please enter mandatory field");
                    text_input_layout_autocomplete.setErrorTextAppearance(R.style.ErrorSize);
                    text_input_layout_autocomplete.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.crimson)));
                    validationSuccess = false;
                } else {
                    if(zipcode_text_field.length() != 5) {
                        text_input_layout_autocomplete.setError("Zipcode length is not equal to 5 digits");
                        text_input_layout_autocomplete.setErrorTextAppearance(R.style.ErrorSize);
                        text_input_layout_autocomplete.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.crimson)));
                        validationSuccess = false;
                    } else {
                        searchQuery.setZipCode(Integer.parseInt(autoCompleteTextViewZipcode.getText().toString()));
                        text_input_layout_autocomplete.setError("");
                        text_input_layout_autocomplete.setErrorTextAppearance(R.style.ErrorSizeZero);
                    }
                }
            } else {
                searchQuery.setDistance("here");
                searchQuery.setZipCode(currentLocationZipCode);
            }
        }
        if(!validationSuccess) {
            Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
            return;
        }
        _searchFragmentListener.OnSearchButtonClicked(searchQuery);
    }

    private void clearAllFields(){
        editTextKeyword.setText("");
        checkBoxNew.setSelected(false);
        checkBoxUsed.setSelected(false);
        checkBoxUnspecified.setSelected(false);
        checkBoxLocalPickup.setSelected(false);
        checkBoxFreeShipping.setSelected(false);
        checkBoxNearby.setChecked(false);
        editTextMilesFrom.setText("");
        radioButtonCurrentLocation.setChecked(true);
        radioButtonZipcode.setChecked(false);
        autoCompleteTextViewZipcode.setText("");
        text_input_layout_keyword.setError("");
        text_input_layout_keyword.setErrorTextAppearance(R.style.ErrorSizeZero);
        text_input_layout_autocomplete.setError("");
        text_input_layout_autocomplete.setErrorTextAppearance(R.style.ErrorSizeZero);

    }
}
