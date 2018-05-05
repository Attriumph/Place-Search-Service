package lingquan.firstApp.placesearch.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.BaseUrl;
import lingquan.firstApp.placesearch.activity.PlacesSearchItemActivity;
import lingquan.firstApp.placesearch.activity.WebViewActivity;
import lingquan.firstApp.placesearch.jsonscn.json2bean.Details;
import lingquan.firstApp.placesearch.jsonscn.json2bean.JsonsRootBean;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;
import lingquan.firstApp.placesearch.places_id_detail.PlacesSearchItemDetailObj;


public class PlacesSearchItemInfoFragment extends Fragment {
    private static final String TAG =PlacesSearchItemInfoFragment.class.getSimpleName() ;
    private PlacesSearchResultObj.PlacesSearchResultItemObj obj;
    private RatingBar ratingBar;
    private TextView address;
    private TextView phoneNum;
    private TextView price;
    private TextView page;
    private TextView site;
    private Context context;
    private View view;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        obj = (PlacesSearchResultObj.PlacesSearchResultItemObj) arguments.getSerializable("data");
    }

    private void startCall(){
        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum.getText()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startCall();
            }
        } else {
            // Permission denied.
        }
    }

    public void updateUI(PlacesSearchItemDetailObj obj) {
        view.setVisibility(View.VISIBLE);
        if (obj.getResult().getRating()==0){
            ((ViewGroup) ratingBar.getParent().getParent()).setVisibility(View.GONE);
        } else {
            ratingBar.setRating(Float.valueOf((float) obj.getResult().getRating()));
        }
        address.setText(obj.getResult().getFormattedAddress());
        if (TextUtils.isEmpty(obj.getResult().getFormattedPhoneNumber())){
            ((ViewGroup) phoneNum.getParent()).setVisibility(View.GONE);
        }else {
            phoneNum.setText(obj.getResult().getFormattedPhoneNumber());
        }
        String priceLevel = obj.getResult().getPriceLevel();
        if (TextUtils.isEmpty(priceLevel)) {
            ((ViewGroup) price.getParent()).setVisibility(View.GONE);
        } else {
            StringBuffer buffer = new StringBuffer();
            for (int index = 0; index < Integer.valueOf(priceLevel); index++) {
                buffer.append("$");
            }
            price.setText(buffer.toString());
        }
        if (TextUtils.isEmpty(obj.getResult().getUrl())){
            ((ViewGroup) page.getParent()).setVisibility(View.GONE);
        } else {
            page.setText(obj.getResult().getUrl());
        }
        if (TextUtils.isEmpty(obj.getResult().getWebsite())){
            ((ViewGroup) site.getParent()).setVisibility(View.GONE);
        }else {
            site.setText(obj.getResult().getWebsite());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.places_search_item_info_fragment, container, false);
        ratingBar = view.findViewById(R.id.rating_bar);
        address = view.findViewById(R.id.address);
        phoneNum = view.findViewById(R.id.phone_number);
        phoneNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        price = view.findViewById(R.id.price);
        page = view.findViewById(R.id.page);
        page.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        site = view.findViewById(R.id.site);
        site.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        view.setVisibility(View.INVISIBLE);
        String place_id = obj.getPlace_id();

        phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum.getText()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(page.getText().toString());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(site.getText().toString());
                intent.setData(content_url);
                startActivity(intent);
            }
        });



        RequestQueue mQueue = Volley.newRequestQueue(context);
        Log.v("url:", BaseUrl.Place_id_info_url + place_id);// the url can get the detail json
        StringRequest request = new StringRequest(Request.Method.GET, BaseUrl.Place_id_info_url + place_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PlacesSearchItemDetailObj infoObj = new Gson().fromJson(response, PlacesSearchItemDetailObj.class);
                updateUI(infoObj);
                if (context instanceof PlacesSearchItemActivity) {
                    ((PlacesSearchItemActivity) context).requsetNumberAdd();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"API failure",Toast.LENGTH_LONG).show();
                if (context instanceof PlacesSearchItemActivity) {
                    ((PlacesSearchItemActivity) context).requsetNumberAdd();
                }
            }
        });
        mQueue.add(request);

        return view;
    }

    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.CALL_PHONE);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }
}
