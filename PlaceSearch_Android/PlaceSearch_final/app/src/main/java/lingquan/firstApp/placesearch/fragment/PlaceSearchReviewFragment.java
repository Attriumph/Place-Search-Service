package lingquan.firstApp.placesearch.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.BaseUrl;
import lingquan.firstApp.placesearch.activity.PlacesSearchItemActivity;
import lingquan.firstApp.placesearch.adapter.PlaceSearchReviewAdapter;
import lingquan.firstApp.placesearch.adapter.YelpReviewAdapter;
import lingquan.firstApp.placesearch.comparator.HighRateComparator;
import lingquan.firstApp.placesearch.comparator.HighRateComparatorYelp;
import lingquan.firstApp.placesearch.comparator.LeastRecentComparator;
import lingquan.firstApp.placesearch.comparator.LeastRecentComparatorYelp;
import lingquan.firstApp.placesearch.comparator.LowRateComparator;
import lingquan.firstApp.placesearch.comparator.LowRateComparatorYelp;
import lingquan.firstApp.placesearch.comparator.MostRecentComparator;
import lingquan.firstApp.placesearch.comparator.MostRecentComparatorYelp;
import lingquan.firstApp.placesearch.jsonscn.json2bean.Details;
import lingquan.firstApp.placesearch.jsonscn.json2bean.Review;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;
import lingquan.firstApp.placesearch.places_id_detail.PlacesSearchItemDetailObj;
import lingquan.firstApp.placesearch.places_id_detail.Result;


public class PlaceSearchReviewFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private PlaceSearchReviewAdapter reviewAdapter = null;
    private ListView list_review;
    private Context context;
    String[] reviewType = {"Google review", "Yelp review"};
    String[] reviewOrder = {"Default order", "Highest rating", "Lowest rating", "Most recent", "Least recent"};
    String[] parsedAdd;
    String place_name;
    String address1;
    String address2;
    String state;
    String city;
    Details details;
    Spinner order_spin;
//    Result details;
    Details originDetails;
    String reviewTypeGlobal = "Google review";
    JSONArray yelpReviewRes;
    JSONArray originYelpReviewRes;
    List<Review> reviews;
    private View layout;
    private View review_none_txt;
    private PlacesSearchResultObj.PlacesSearchResultItemObj obj;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_review, container, false);

        list_review = (ListView) view.findViewById(R.id.reviewList);
        layout = view.findViewById(R.id.layout);
        review_none_txt = view.findViewById(R.id.review_none_txt);
        Bundle arguments = getArguments();
        obj = (PlacesSearchResultObj.PlacesSearchResultItemObj) arguments.getSerializable("data");
        String place_id = obj.getPlace_id();
        context = getActivity();

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String details_url = BaseUrl.Place_id_info_url + place_id;
        Log.v("url:", BaseUrl.Place_id_info_url + place_id);// the url can get the detail json
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, details_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                Log.v("the response is ", response.toString());
                                JSONObject json_details = response.getJSONObject("result");
                                String placeDetails = json_details.toString();
                                Gson gson = new Gson();
                                details = gson.fromJson(placeDetails, new TypeToken<Details>() {}.getType());
                                originDetails = details.myclone();
                                yelpRequest(details,view);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                if (context instanceof PlacesSearchItemActivity){
                                    ((PlacesSearchItemActivity)context).requsetNumberAdd();
                                }
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("the error is ",error.toString());
                        Toast.makeText(getContext(),"API failure",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        if (context instanceof PlacesSearchItemActivity){
                            ((PlacesSearchItemActivity)context).requsetNumberAdd();
                        }
                    }
                });
        mQueue.add(jsonRequest);

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        switch (parent.getId()) {
            //click review type
            case R.id.review_type:

                ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_spinner_item, reviewOrder);
                aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                order_spin.setAdapter(aa2);
                if (reviewType[position].equals("Yelp review")) {
                    reviewTypeGlobal = "Yelp review";
                    YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context, yelpReviewRes);
                    list_review.setAdapter(yelpReviewAdapter);
                    if (yelpReviewRes == null||yelpReviewRes.length() == 0){
                        layout.setVisibility(View.GONE);
                        review_none_txt.setVisibility(View.VISIBLE);
                    }else {
                        layout.setVisibility(View.VISIBLE);
                        review_none_txt.setVisibility(View.GONE);
                    }

                } else {
                    reviewTypeGlobal = "Google review";
                    reviewAdapter = new PlaceSearchReviewAdapter(context, originDetails.getReviews());
                    list_review.setAdapter(reviewAdapter);
                    if (originDetails.getReviews().size()==0){
                        layout.setVisibility(View.GONE);
                        review_none_txt.setVisibility(View.VISIBLE);
                    }else {
                        layout.setVisibility(View.VISIBLE);
                        review_none_txt.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.review_order:
                if (reviewOrder[position].equals("Default order")) {
                    if (reviewTypeGlobal.equals("Google review")) {
                        Log.v("defa:", "default order");
                        reviewAdapter = new PlaceSearchReviewAdapter(context, originDetails.getReviews());
                        list_review.setAdapter(reviewAdapter);
                        if (originDetails.getReviews().size() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    } else{ // yelp review
                        YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context,originYelpReviewRes);
                        list_review.setAdapter(yelpReviewAdapter);
                        if (originYelpReviewRes.length()==0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    }

                } else if (reviewOrder[position].equals("Highest rating")) {
                    if (reviewTypeGlobal.equals("Google review")) {
                        Collections.sort(details.getReviews(), new HighRateComparator());
                        reviewAdapter = new PlaceSearchReviewAdapter(context, details.getReviews());
                        list_review.setAdapter(reviewAdapter);
                        if (details.getReviews().size() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray sortedJsonArray = new JSONArray();
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            try{jsonValues.add(yelpReviewRes.getJSONObject(i));}
                            catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        Collections.sort( jsonValues, new HighRateComparatorYelp());
//
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                        YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context,sortedJsonArray);
                        list_review.setAdapter(yelpReviewAdapter);
                        if (sortedJsonArray.length() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }

                    }
                } else if (reviewOrder[position].equals("Lowest rating")) {
                    if (reviewTypeGlobal.equals("Google review")) {
                        Collections.sort(details.getReviews(),  new LowRateComparator());
                        reviewAdapter = new PlaceSearchReviewAdapter(context, details.getReviews());
                        list_review.setAdapter(reviewAdapter);
                        if (details.getReviews().size() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray sortedJsonArray = new JSONArray();
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            try{jsonValues.add(yelpReviewRes.getJSONObject(i));}
                            catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        Collections.sort( jsonValues, new LowRateComparatorYelp());
//
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                        YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context,sortedJsonArray);
                        list_review.setAdapter(yelpReviewAdapter);
                        if (sortedJsonArray.length()==0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }

                    }

                } else if (reviewOrder[position].equals("Most recent")) {
                    if (reviewTypeGlobal.equals("Google review")) {
                        Collections.sort(details.getReviews(), new MostRecentComparator());
                        reviewAdapter = new PlaceSearchReviewAdapter(context, details.getReviews());
                        list_review.setAdapter(reviewAdapter);
                        if (details.getReviews().size() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray sortedJsonArray = new JSONArray();
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            try{jsonValues.add(yelpReviewRes.getJSONObject(i));}
                            catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        Collections.sort( jsonValues, new MostRecentComparatorYelp());
//
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                        YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context,sortedJsonArray);
                        list_review.setAdapter(yelpReviewAdapter);
                        if (sortedJsonArray.length() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    }

                } else if (reviewOrder[position].equals("Least recent")) {
                    if (reviewTypeGlobal.equals("Google review")) {
                        Collections.sort(details.getReviews(), new LeastRecentComparator());
                        reviewAdapter = new PlaceSearchReviewAdapter(context, details.getReviews());
                        list_review.setAdapter(reviewAdapter);
                        if (details.getReviews().size() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray sortedJsonArray = new JSONArray();
                        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            try{jsonValues.add(yelpReviewRes.getJSONObject(i));}
                            catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        Collections.sort( jsonValues, new LeastRecentComparatorYelp());
//
                        for (int i = 0; i < yelpReviewRes.length(); i++) {
                            sortedJsonArray.put(jsonValues.get(i));
                        }
                        YelpReviewAdapter yelpReviewAdapter = new YelpReviewAdapter(context,sortedJsonArray);
                        list_review.setAdapter(yelpReviewAdapter);
                        if (sortedJsonArray.length() == 0){
                            layout.setVisibility(View.GONE);
                            review_none_txt.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.VISIBLE);
                            review_none_txt.setVisibility(View.GONE);
                        }


                    }
                }
                break;
                default:
                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public  void yelpRequest(Details details,View view){
        String formmatted_address = details.getFormatted_address();
        parsedAdd = formmatted_address.split(",");
        place_name = details.getName();
        Log.v("add list", parsedAdd.toString());
        address1 = parsedAdd[0];
        address2 = parsedAdd[1].trim() + "," + parsedAdd[2].trim();
        city = parsedAdd[parsedAdd.length - 3].trim();
        String[] arr2 = parsedAdd[parsedAdd.length - 2].split(" ");
        state = arr2[1].trim();

        reviewAdapter = new PlaceSearchReviewAdapter(context, details.getReviews());
        list_review.setAdapter(reviewAdapter);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner type_spin = (Spinner) view.findViewById(R.id.review_type);
        type_spin.setOnItemSelectedListener(this);
        order_spin = (Spinner) view.findViewById(R.id.review_order);
        order_spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the  list
        ArrayAdapter aa1 = new ArrayAdapter(context, android.R.layout.simple_spinner_item, reviewType);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        type_spin.setAdapter(aa1);

        //Creating the ArrayAdapter instance having the  list
        ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_spinner_item, reviewOrder);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        order_spin.setAdapter(aa2);



        // request yelp review
        String yelpUrl = "http://placesearch-env.us-west-1.elasticbeanstalk.com/yelp/?address1=";
        yelpUrl += URLEncoder.encode(address1) + "&address2=" + URLEncoder.encode(address2 )+ "&city=" + URLEncoder.encode(city) + "&name=" + URLEncoder.encode(place_name) + "&state=" + state;
        Log.v("yelpURl", yelpUrl);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                yelpUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        yelpReviewRes = response;
                        originYelpReviewRes = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"API failure",Toast.LENGTH_LONG).show();

                    }
                }
        );
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }
}
