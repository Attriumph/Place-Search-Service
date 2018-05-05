package lingquan.firstApp.placesearch.fragment;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import  lingquan.firstApp.placesearch.R;
import  lingquan.firstApp.placesearch.Utils.BaseUrl;
import  lingquan.firstApp.placesearch.Utils.Utils;
import lingquan.firstApp.placesearch.adapter.CustomAutoCompleteAdapter;
import  lingquan.firstApp.placesearch.jsonscn.json2bean.JsonsRootBean;
import  lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;
import  lingquan.firstApp.placesearch.task.ParserTask;



public class PlaceSearchItemMapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    private AutoCompleteTextView mStartAddress;
    private CustomAutoCompleteAdapter adapter;
    private Context context;
    private String Latitude;
    private String Longitude;
    private PlacesSearchResultObj.PlacesSearchResultItemObj obj;
    private Spinner spinner;
    private String mode ="DRIVING";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = (PlacesSearchResultObj.PlacesSearchResultItemObj) getArguments().getSerializable("data");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.places_search_item_map_fragment, container, false);
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mStartAddress = view.findViewById(R.id.start_address);
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = (String) parent.getAdapter().getItem(position);
                if (TextUtils.isEmpty(mStartAddress.getText().toString())) return;
                addRouteonMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback(){

            @Override
            public void onMapReady(GoogleMap googleMap) {
                double latitude = Double.valueOf(obj.getGeometry().getLocation().getLat());
                double longitude = Double.valueOf(obj.getGeometry().getLocation().getLng());
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude)).title(obj.getName());
                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                // Perform any camera updates here
                Marker marker1 = googleMap.addMarker(marker);
                marker1.showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                PlaceSearchItemMapFragment.this.googleMap = googleMap;
            }
        });
        adapter = new CustomAutoCompleteAdapter(context);
        mStartAddress.setAdapter(adapter);
        mStartAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final StringBuffer buffer = new StringBuffer();
                buffer.append(BaseUrl.Geocoding_URL);
                String text = mStartAddress.getText().toString();
                buffer.append(text);
//                text.replaceAll(" ", "+");
//                buffer.append(text).append("&");
//                buffer.append("key=").append(Utils.GOOGLE_AUTHER_KEY);
                RequestQueue mQueue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(Request.Method.GET, buffer.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] latlng = response.substring(1,response.length() - 1).split(",");
                        Latitude = latlng[0];
                        Longitude = latlng[1];
                        addRouteonMap();
                        Log.v("latlng is:",Latitude+","+Longitude);
//                       JsonsRootBean obj = new Gson().fromJson(response, JsonsRootBean.class);
//                        if (obj.getResults() != null && obj.getResults().size() > 0) {
//                            Latitude = String.valueOf(obj.getResults().get(0).getGeometry().getLocation().getLat());
//                            Longitude = String.valueOf(obj.getResults().get(0).getGeometry().getLocation().getLng());
//                            addRouteonMap();
//                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                mQueue.add(request);
            }
        });
        return view;
    }

    private void addRouteonMap(){
        googleMap.clear();
        double latitude = Double.valueOf(obj.getGeometry().getLocation().getLat());
        double longitude = Double.valueOf(obj.getGeometry().getLocation().getLng());
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(obj.getName());
        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        // Perform any camera updates here
        MarkerOptions marker1 = new MarkerOptions().position(
                new LatLng(Double.valueOf(Latitude),Double.valueOf(Longitude))).title(mStartAddress.getText().toString());
        marker1.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        Marker marker2 = googleMap.addMarker(marker1);
        googleMap.addMarker(marker);
        marker2.showInfoWindow();



        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.valueOf(Latitude),Double.valueOf(Longitude))).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        String directionsUrl = getDirectionsUrl(new LatLng(Double.valueOf(Latitude), Double.valueOf(Longitude)), new LatLng(Double.valueOf(obj.getGeometry().getLocation().getLat()), Double.valueOf(obj.getGeometry().getLocation().getLng())));
        Log.v("the direct url is ",directionsUrl);

        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, directionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ParserTask parserTask = new ParserTask(googleMap,context);
                parserTask.execute(response);

                double latitude = Double.valueOf(obj.getGeometry().getLocation().getLat());
                double longitude = Double.valueOf(obj.getGeometry().getLocation().getLng());
                Log.v("lnglat",latitude+","+longitude+" "+Latitude+","+Longitude);
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLngBounds.Builder mapBound = new LatLngBounds.Builder();
                mapBound.include(new LatLng(latitude, longitude)).include(new LatLng(Double.valueOf(Latitude),Double.valueOf(Longitude)));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapBound.getCenter(), 12));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(mapBound.build(), 12);
                googleMap.moveCamera(cameraUpdate);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode1 = "mode="+mode.toLowerCase();

        String parameters = str_origin + "&" + str_dest + "&"+ mode1 +"&key="+Utils.GOOGLE_AUTHER_KEY;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;
        System.out.println("getDerectionsURL--->: " + url);
        return url;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

