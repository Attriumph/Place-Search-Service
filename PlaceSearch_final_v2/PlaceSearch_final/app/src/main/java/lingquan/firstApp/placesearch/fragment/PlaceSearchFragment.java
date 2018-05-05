package lingquan.firstApp.placesearch.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.BaseUrl;
import lingquan.firstApp.placesearch.Utils.Utils;
import lingquan.firstApp.placesearch.activity.PlacesSearchResultActivtity;
import lingquan.firstApp.placesearch.adapter.CustomAutoCompleteAdapter;
import lingquan.firstApp.placesearch.jsonscn.json2bean.JsonsRootBean;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;



public class PlaceSearchFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener {


//    public static String Latitude = "151.1957362";
//    public static String Longitude = "-33.8670522";
    public static String Latitude ;
    public static String Longitude ;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final String TAG = PlaceSearchFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Context context;
    private EditText mPlaceSearchKeyWord;
    private Spinner mPlaceSearchCategory;
    private EditText mPlaceSearchDistance;
    private RadioButton mPlaceSearchCurrentLocation;
    private RadioButton mPlaceSearchOtherLocation;
    private AutoCompleteTextView mPlaceSearchLocation;
    private RadioGroup group;
    private Button mPlaceSearchSubmit;
    private Button mPlaceSearchClear;
    private FusedLocationProviderClient mFusedLocationClient;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private String category = "Default";
    private String formatted_category;
    private ProgressDialog dialog;
    private String oldLocation;
    private CustomAutoCompleteAdapter adapter;
    private TextView keywordReminder;
    private TextView locationReminder;
    private String[] latlng;


    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private int AUTO_COMP_REQ_CODE = 0x01;
    private JsonsRootBean obj;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching Results");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);
        adapter = new CustomAutoCompleteAdapter(context);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.
            }
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                Log.e("lingquan", "the current location is " + mCurrentLocation);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions();
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }
                    }
                });
    }

    private void updateLocationUI() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.places_search_fragment, container, false);
        mPlaceSearchKeyWord = convertView.findViewById(R.id.search_places_keyword);
        mPlaceSearchCategory = convertView.findViewById(R.id.spinner);
        mPlaceSearchDistance = convertView.findViewById(R.id.place_search_distances);
        mPlaceSearchCurrentLocation = convertView.findViewById(R.id.current_location);
        mPlaceSearchOtherLocation = convertView.findViewById(R.id.other_location);
        mPlaceSearchLocation = convertView.findViewById(R.id.place_search_location);
        mPlaceSearchSubmit = convertView.findViewById(R.id.place_search_submit);
        mPlaceSearchClear = convertView.findViewById(R.id.place_search_clear);
        group = convertView.findViewById(R.id.group);
        keywordReminder = convertView.findViewById(R.id.reminder_keyword);
        locationReminder = convertView.findViewById(R.id.reminder_loaction);
        keywordReminder.setVisibility(View.GONE);
        locationReminder.setVisibility(View.GONE);
        group.setOnCheckedChangeListener(this);
        group.check(R.id.current_location);
        mPlaceSearchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                } else {
                    keywordReminder.setVisibility(View.GONE);
                }
            }
        });

        mPlaceSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                } else {
                    locationReminder.setVisibility(View.GONE);
                }
            }
        });

        mPlaceSearchLocation.setAdapter(adapter);
        mPlaceSearchLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obj = null;
                final StringBuffer buffer = new StringBuffer();
                buffer.append(BaseUrl.Geocoding_URL);
                String text = mPlaceSearchLocation.getText().toString();
                text.replaceAll(" ", "+");
                buffer.append(text);
                Log.v("latlng url:",buffer.toString());

                RequestQueue mQueue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(Request.Method.GET, buffer.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("latlng res:",response);
                        latlng = response.substring(1,response.length() - 1).split(",");
                        Latitude = latlng[0];
                        Longitude = latlng[1];
                        Log.v("latlng is:",Latitude+","+Longitude);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                });
                mQueue.add(request);
            }
        });
        mPlaceSearchSubmit.setOnClickListener(this);
        mPlaceSearchClear.setOnClickListener(this);
        mPlaceSearchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getAdapter().getItem(position);

                category = category.toLowerCase();
                formatted_category = category.replaceAll(" ","_");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return convertView;
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mPlaceSearchCurrentLocation) {
            if (isChecked) {
                mPlaceSearchOtherLocation.setChecked(false);
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            }
        } else if (buttonView == mPlaceSearchOtherLocation) {
            if (isChecked) mPlaceSearchCurrentLocation.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPlaceSearchSubmit) {
            String text = mPlaceSearchKeyWord.getText().toString().replaceAll(" ","");
            Log.v("text-->",text);

            if ((TextUtils.isEmpty(mPlaceSearchKeyWord.getText())||text.length() == 0) && mPlaceSearchOtherLocation.isChecked()) {
                Log.v("two","here");
                keywordReminder.setVisibility(View.VISIBLE);
                locationReminder.setVisibility(View.VISIBLE);
                Toast.makeText(context, "please fix all fields with errors", Toast.LENGTH_LONG).show();
                return;
            }
            keywordReminder.setVisibility(View.GONE);
            locationReminder.setVisibility(View.GONE);




            if (TextUtils.isEmpty(mPlaceSearchKeyWord.getText())||text.length() == 0  && mPlaceSearchCurrentLocation.isChecked()) {
                Log.v("one","here");
                keywordReminder.setVisibility(View.VISIBLE);
                Toast.makeText(context, "please fix all fields with errors", Toast.LENGTH_LONG).show();
                return;
            }
            keywordReminder.setVisibility(View.GONE);



            if (mPlaceSearchCurrentLocation.isChecked()) {
                if (mCurrentLocation == null) {
                    Toast.makeText(context, "can not find the current places,wait a minutes..", Toast.LENGTH_LONG).show();
                    return;
                }
                Latitude = String.valueOf(mCurrentLocation.getLatitude());
                Longitude = String.valueOf(mCurrentLocation.getLongitude());
            } else if (mPlaceSearchOtherLocation.isChecked()) {
                if (TextUtils.isEmpty(mPlaceSearchLocation.getText())) {
                    locationReminder.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "please fix all fields with errors", Toast.LENGTH_LONG).show();
                    return;
                }
                locationReminder.setVisibility(View.GONE);
                if (latlng == null) {
                    Toast.makeText(context, "can not find the  places position,wait a minutes..", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            searchPlaces();

        } else if (v == mPlaceSearchClear) {
            clearSearchPlace();
        }
    }

    private void clearSearchPlace() {
        mPlaceSearchKeyWord.setText("");
        mPlaceSearchCategory.setSelection(0);
        mPlaceSearchCurrentLocation.setChecked(true);
        mPlaceSearchOtherLocation.setChecked(false);
        mPlaceSearchDistance.setText("");
        mPlaceSearchLocation.setText("");
        keywordReminder.setVisibility(View.GONE);
        locationReminder.setVisibility(View.GONE);
    }

    private void searchPlaces() {
        dialog.show();
        final StringBuffer buffer = new StringBuffer();
        buffer.append(BaseUrl.SEARCH_URL);
//        buffer.append("http://127.0.0.1:8080/");

        buffer.append("startLng=").append(Longitude);
        buffer.append("&");
        buffer.append("startLat=").append(Latitude);
        buffer.append("&");
        if (!TextUtils.isEmpty(mPlaceSearchDistance.getText())) {
            buffer.append("distance=").append(mPlaceSearchDistance.getText()).append("&");
        } else {
            buffer.append("distance=").append(10).append("&");
        }

        buffer.append("category=").append(formatted_category).append("&keyword=").append(mPlaceSearchKeyWord.getText());
        Log.v("request url",buffer.toString());

        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, buffer.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                PlacesSearchResultObj obj = new Gson().fromJson(response, PlacesSearchResultObj.class);
                Intent intent = new Intent(context, PlacesSearchResultActivtity.class);
                intent.putExtra("data", obj);
                intent.putExtra("url", buffer.toString());
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(),"No network",Toast.LENGTH_LONG).show();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String location = s.toString();
        if (TextUtils.isEmpty(location)) return;
        if (location.equals(oldLocation)) return;
        try {
            Intent build = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).zzh(s.toString()).
                    build((Activity) context);
            startActivityForResult(build, AUTO_COMP_REQ_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        oldLocation = location;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(context, data);
            Log.e("lingquan", "the task is " + resultCode + " " + requestCode + "     " + PlaceAutocomplete.getPlace(context, data).toString());
            oldLocation = place.getAddress().toString();
            mPlaceSearchLocation.setText(place.getAddress());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.current_location){
            mPlaceSearchLocation.setText("");
            Log.v("try to clear location",mPlaceSearchLocation.getText().toString());
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            locationReminder.setVisibility(View.GONE);
            mPlaceSearchLocation.setInputType(EditorInfo.TYPE_NULL);
        }else if (checkedId==R.id.other_location){
            mPlaceSearchLocation.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
    }
}
