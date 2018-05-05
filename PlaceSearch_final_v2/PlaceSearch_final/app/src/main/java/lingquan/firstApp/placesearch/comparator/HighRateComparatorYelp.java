package lingquan.firstApp.placesearch.comparator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class HighRateComparatorYelp implements Comparator<JSONObject> {

        private static final String RATING = "rating";
        @Override
        public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                int valB = 0;

                try {
                        valA = Integer.valueOf(a.getString(RATING));
                        valB = Integer.valueOf(b.getString(RATING));
                }
                catch (JSONException e) {
                        //do something
                }

                return valB-valA;
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
        }
}
