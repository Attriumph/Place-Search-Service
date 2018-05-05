package lingquan.firstApp.placesearch.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.BaseUrl;
import lingquan.firstApp.placesearch.Utils.SharePreferenceUtils;
import lingquan.firstApp.placesearch.adapter.MyFragmentPagerAdapter;
import lingquan.firstApp.placesearch.adapter.TabPageIndicatorFragmentAdapter;
import lingquan.firstApp.placesearch.fragment.PlaceSearchFragment;
import lingquan.firstApp.placesearch.fragment.PlaceSearchItemMapFragment;
import lingquan.firstApp.placesearch.fragment.PlaceSearchReviewFragment;
import lingquan.firstApp.placesearch.fragment.PlacesSearchItemInfoFragment;
import lingquan.firstApp.placesearch.fragment.PlacesSearchItemPhotoFragment;
import lingquan.firstApp.placesearch.fragment.PlacesSearchItemReviewerFragment;
import lingquan.firstApp.placesearch.jsonscn.json2bean.Details;
import lingquan.firstApp.placesearch.obj.MyFavoriteChangedObj;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;
import lingquan.firstApp.placesearch.places_id_detail.PlacesSearchItemDetailObj;
import lingquan.firstApp.placesearch.viewpagerindicator.TabPageIndicator;


public class PlacesSearchItemActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView mBack;
    private TextView titleName;
    private ImageView shareImg;
    private ImageView favoriteImg;
    private TabPageIndicator tab;
    private ViewPager pager;
    private int requestNumber = 0;
    private String[] title = new String[]{"INFO", "PHOTOS", "MAP", "REVIEWS"};
    private Integer[] drawable = new Integer[]{R.drawable.info_tab, R.drawable.photo_tab, R.drawable.map_tab, R.drawable.review_tab};
    private PlacesSearchItemInfoFragment info;
    private PlacesSearchItemPhotoFragment photoFragment;
    private PlaceSearchItemMapFragment map;
    //    private PlacesSearchItemReviewerFragment reviewer;
    private PlaceSearchReviewFragment reviewer;
    private TabPageIndicatorFragmentAdapter<Fragment> adapter;
    private PlacesSearchResultObj.PlacesSearchResultItemObj obj;
    private PlacesSearchItemDetailObj infoObj;
    private View layout;
    private ProgressDialog dialog;
    private Details details;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching Details");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        context =  this;
        setContentView(R.layout.places_search_result_item_activity);
        obj = (PlacesSearchResultObj.PlacesSearchResultItemObj) getIntent().getSerializableExtra("data");
        mBack = findViewById(R.id.back);
        titleName = findViewById(R.id.name);
        shareImg = findViewById(R.id.share);
        favoriteImg = findViewById(R.id.favorite);
        layout = findViewById(R.id.layout);

        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.viewpager);
        mBack.setOnClickListener(this);
        favoriteImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        layout.setVisibility(View.INVISIBLE);
        dialog.show();

        init();
        initData();
        reqDetails ();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyFavoriteChangedObj item) {
        boolean result = SharePreferenceUtils.getInstance(this).isMyFavorite(obj);
        if (result) favoriteImg.setImageResource(R.drawable.favor_sd_white);
        else favoriteImg.setImageResource(R.drawable.favor_emp_white);
    }

    public void requsetNumberAdd() {
        requestNumber++;
        if (requestNumber == 3) {
            layout.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        titleName.setText(obj.getName());
        boolean result = SharePreferenceUtils.getInstance(this).isMyFavorite(obj);
        if (result) favoriteImg.setImageResource(R.drawable.favor_sd_white);
        else favoriteImg.setImageResource(R.drawable.favor_emp_white);
    }

    private void init() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", obj);

        info = new PlacesSearchItemInfoFragment();
        photoFragment = new PlacesSearchItemPhotoFragment();
        map = new PlaceSearchItemMapFragment();
        reviewer = new PlaceSearchReviewFragment();

        info.setArguments(bundle);
        photoFragment.setArguments(bundle);
        photoFragment.setArguments(bundle);
        map.setArguments(bundle);
        reviewer.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(info);
        fragments.add(photoFragment);
        fragments.add(map);
        fragments.add(reviewer);
        tab.setShowItems(4);
        adapter = new TabPageIndicatorFragmentAdapter(getSupportFragmentManager(), fragments, this, Arrays.asList(title), Arrays.asList(drawable));
        pager.setAdapter(adapter);
        tab.setViewPager(pager);
        pager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            finish();
        } else if (v == favoriteImg) {
            boolean result = SharePreferenceUtils.getInstance(this).isMyFavorite(obj);
            if (result) {
                Toast.makeText(this,obj.getName()+"was removed from favorites",Toast.LENGTH_LONG).show();
                SharePreferenceUtils.getInstance(this).removeMyFavorite(obj);
            } else {
                Toast.makeText(this,obj.getName()+"was added to favorites",Toast.LENGTH_LONG).show();
                SharePreferenceUtils.getInstance(this).addMyFavorite(obj);
            }
            EventBus.getDefault().post(new MyFavoriteChangedObj());
        } else if (shareImg == v){
            Log.v("share","into twitter");
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");

            String twitter = "https://twitter.com/intent/tweet?text="+ URLEncoder.encode("Check out "+ obj.getName()
                    +" located at "+obj.getVicinity()+". Website:")+"&url="+details.getWebsite()
                    +"&hashtags=TravelAndEntertainmentSearch";
            Log.v("twitter url",twitter);
            Uri content_url = Uri.parse(twitter);
            Log.v("twitter url",content_url.toString());
            intent.setData(content_url);
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    public void reqDetails () {
        String place_id = obj.getPlace_id();

        RequestQueue mQueue = Volley.newRequestQueue(this);
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"API failure",Toast.LENGTH_LONG).show();
                        Log.i("the error is ",error.toString());
                        error.printStackTrace();

                    }
                });
        mQueue.add(jsonRequest);
    }
}
