package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter {
    public static final String TAG = "CustomAutoCompAdapter";
    private List<Place> dataList;
    private Context mContext;
    private GeoDataClient geoDataClient;

    private CustomAutoCompleteFilter listFilter =
            new CustomAutoCompleteFilter();


    public CustomAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line,
                new ArrayList<Place>());
        mContext = context;
        geoDataClient = Places.getGeoDataClient(mContext, null);


    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Place getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_dropdown_item_1line,
                            parent, false);
        }

        TextView textOne = view.findViewById(android.R.id.text1);
        textOne.setText(dataList.get(position).getPlaceText());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class CustomAutoCompleteFilter extends Filter {
        private Object lock = new Object();
        private Object lockTwo = new Object();
        private boolean placeResults = false;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            placeResults = false;
            final List<Place> placesList = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<Place>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();
                Task<AutocompletePredictionBufferResponse> task
                        = getAutoCompletePlaces(searchStrLowerCase);
                task.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Auto complete prediction successful");
                            AutocompletePredictionBufferResponse predictions = task.getResult();
                            Place autoPlace;
                            for (AutocompletePrediction prediction : predictions) {
                                autoPlace = new Place();
                                autoPlace.setPlaceId(prediction.getPlaceId());
                                autoPlace.setPlaceText(prediction.getFullText(null).toString());
                                placesList.add(autoPlace);
                            }
                            predictions.release();
                            Log.d(TAG, "Auto complete predictions size " + placesList.size());
                        } else {
                            Log.d(TAG, "Auto complete prediction unsuccessful");
                        }
                        placeResults = true;
                        synchronized (lockTwo) {
                            lockTwo.notifyAll();
                        }
                    }
                });

                //wait for the results from asynchronous API call
                while (!placeResults) {
                    synchronized (lockTwo) {
                        try {
                            lockTwo.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
                results.values = placesList;
                results.count = placesList.size();
                Log.d(TAG, "Autocomplete predictions size after wait" + results.count);
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Place>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private Task<AutocompletePredictionBufferResponse> getAutoCompletePlaces(String query) {
            //create autocomplete filter using data from filter Views
            AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();
            Task<AutocompletePredictionBufferResponse> results =
                    geoDataClient.getAutocompletePredictions(query, null,
                            filterBuilder.build());
            return results;
        }
    }

    public class Place {
        private String placeId;
        private String placeText;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getPlaceText() {
            return placeText;
        }

        public void setPlaceText(String placeText) {
            this.placeText = placeText;
        }
        public String toString(){
            return placeText;
        }
    }
}
